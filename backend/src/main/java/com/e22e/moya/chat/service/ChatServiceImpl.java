package com.e22e.moya.chat.service;

import com.e22e.moya.chat.dto.ChatRequestDto;
import com.e22e.moya.chat.dto.ChatResponseDto;
import com.e22e.moya.chat.repository.ExplorationRepositoryChat;
import com.e22e.moya.chat.repository.NpcPosRepositoryChat;
import com.e22e.moya.chat.repository.ParkRepositoryChat;
import com.e22e.moya.chat.repository.SpeciesRepositoryChat;
import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.chatting.Chat;
import com.e22e.moya.common.entity.chatting.Message;
import com.e22e.moya.common.entity.npc.Npc;
import com.e22e.moya.common.entity.npc.NpcPos;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.common.util.ChatAssistant;
import com.e22e.moya.common.util.ChatUtils;
import com.e22e.moya.chat.repository.ChatRepositoryChat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private final ParkRepositoryChat parkRepository;
    private final NpcPosRepositoryChat npcPosRepository;
    private final SpeciesRepositoryChat speciesRepository;
    private final ChatRepositoryChat chatRepository;
    private final ChatAssistant assistant; // AI 챗봇과의 상호작용을 관리하는 객체
    private final RedisTemplate<String, String> redisTemplate; // 타입 변경
    private final ObjectMapper objectMapper; // 새로 추가
    private final ExplorationRepositoryChat explorationRepository;

    public ChatServiceImpl(ParkRepositoryChat parkRepository, NpcPosRepositoryChat npcPosRepository,
        SpeciesRepositoryChat speciesRepository,
        ChatRepositoryChat chatRepository,
        RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper,
        ExplorationRepositoryChat explorationRepository) {
        this.parkRepository = parkRepository;
        this.npcPosRepository = npcPosRepository;
        this.speciesRepository = speciesRepository;
        this.chatRepository = chatRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.explorationRepository = explorationRepository;

        // Assistant 설정에 ContentRetriever 추가
        this.assistant = AiServices.builder(ChatAssistant.class)
            .chatLanguageModel(OpenAiChatModel.builder()
                .apiKey(ChatUtils.OPENAI_API_KEY)
                .temperature(0.7)  // 응답 민감도 조절
                .build())
            .build();
    }

    /**
     * 사용자 메시지 처리, AI 응답 생성, DB 저장
     *
     * @param requestDto 채팅 담은 dto
     * @param npcPosId   npc 위치 id
     * @return 채팅 응답
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ChatResponseDto processUserMessage(ChatRequestDto requestDto, Long npcPosId,
        Long userId, Long parkId, Long explorationId) {

        Exploration exploration = explorationRepository.findById(explorationId)
            .orElseThrow(() -> new EntityNotFoundException("탐험을 찾을 수 없습니다."));

        Chat chat = this.chatRepository.findByNpcPosIdAndUserId(npcPosId, userId)
            .orElse(new Chat());

        boolean isNewChat = (chat.getId() == null);

        // 새로운 chat 객체인 경우 npcPos와 userId 설정
        if (isNewChat) {
            NpcPos npcPos = npcPosRepository.findById(npcPosId).orElseThrow(() ->
                new EntityNotFoundException("npc위치 찾을 수 없음"));
            chat.setNpcPos(npcPos);
            chat.setUserId(userId);
        }

        List<Message> recentMessages = getRecentMessages(chat);
        String context = buildContext(recentMessages);

        Message userMessage = createUserMessage(requestDto.getMessage());
        chat.getMessages().add(userMessage);

        Park park = exploration.getPark();
        List<Species> parkSpecies = getParkSpecies(parkId);

        NpcPos npcPos = npcPosRepository.findById(npcPosId)
            .orElseThrow(() -> new EntityNotFoundException("NPC를 찾을 수 없습니다."));
        Npc npc = npcPos.getParkNpc().getNpc();

        String sayHi = "";
        if (isNewChat && exploration.getStartTime().isAfter(LocalDateTime.now().minusHours(24))) {
            sayHi = "또 오셨네요! 반갑습니다. ";
        }

        String prompt = buildPrompt(context, userMessage, park, npc, parkSpecies, sayHi);
        String response = this.assistant.answer(prompt);

        response = postProcessAIResponse(response);

        // 임시 답변
        response = "싸피 뒷뜰에 오신 것을 환영해요. 이 공원에서는 강아지풀, 단풍잎, 솔방울을 볼 수 있어요.";

        Message aiMessage = createAiMessage(response);

        chat.getMessages().add(aiMessage);

        this.chatRepository.save(chat);

        return new ChatResponseDto(response);
    }

    /**
     * 공원의 동식물 조회
     *
     * @param parkId 공원 id
     */
    private List<Species> getParkSpecies(Long parkId) {
        String cacheKey = "park_species_" + parkId;
        String cachedData = redisTemplate.opsForValue().get(cacheKey);
        List<Species> parkSpecies;

        if (cachedData == null) {
            parkSpecies = speciesRepository.findAllSpeciesByParkId(parkId);
            try {
                String serializedData = objectMapper.writeValueAsString(parkSpecies);
                redisTemplate.opsForValue()
                    .set(cacheKey, serializedData);
            } catch (Exception e) {
                log.error("parkSpecies 직렬화 불가", e);
            }
        } else { // 캐시에 정보가 들어있다면
            try {
                parkSpecies = objectMapper.readValue(cachedData,
                    new TypeReference<>() {
                    });
            } catch (Exception e) {
                log.error("parkSpecies 역직렬화 불가", e);
                parkSpecies = speciesRepository.findAllSpeciesByParkId(parkId);
            }
        }

        return parkSpecies;
    }

    /**
     * 새로운 동식물이 발견되었다면 반영
     *
     * @param parkId 공원 id
     */
    @Override
    public void updateSpeciesCache(Long parkId, Species newSpecies) {
        System.out.println("redis에 새롭게 찾은 종 추가");
        String cacheKey = "park_species_" + parkId;
        try {
            String cachedData = redisTemplate.opsForValue().get(cacheKey);
            if (cachedData != null) {
                List<Species> parkSpecies = objectMapper.readValue(cachedData,
                    new TypeReference<>() {
                    });

                parkSpecies.add(newSpecies);

                String updatedData = objectMapper.writeValueAsString(parkSpecies);

                redisTemplate.opsForValue().set(cacheKey, updatedData);
                log.info("새로운 종 추가 완료: {}", newSpecies.getName());
            }
        } catch (Exception e) {
            log.error("캐시 업데이트 중 오류 발생", e);
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 밤 12시에
    public void clearParkSpeciesCache() {
        Set<String> keys = redisTemplate.keys("park_species_*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("{}개의 동식물 캐시에서 제거", keys.size());
        }
    }

    private List<Message> getRecentMessages(Chat chat) {
        List<Message> messages = new ArrayList<>(chat.getMessages());
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return m2.getMessageTime().compareTo(m1.getMessageTime());
            }
        });
        return messages.subList(0, Math.min(messages.size(), 20));
    }

    private String buildContext(List<Message> messages) {
        StringBuilder context = new StringBuilder();
        for (Message message : messages) {
            context.append(message.isUserMessage() ? "User: " : "AI: ")
                .append(message.getContent())
                .append("\n");
        }
        return context.toString();
    }

    private Message createUserMessage(String content) {
        Message message = new Message();
        message.setContent(content);
        message.setMessageTime(LocalDateTime.now());
        message.setUserMessage(true);
        return message;
    }

    private Message createAiMessage(String content) {
        Message message = new Message();
        message.setContent(content);
        message.setMessageTime(LocalDateTime.now());
        message.setUserMessage(false);
        return message;
    }

    private String buildPrompt(String context, Message userMessage, Park park, Npc npc,
        List<Species> parkSpecies, String sayHi) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("당신은 ").append(npc.getName()).append("이라는 이름의 공원 안내 AI 도우미입니다. ")
            .append("현재 ").append(park.getName()).append(" 공원에 있습니다.\n\n");

        prompt.append("역할 및 성격:\n");
        prompt.append("- 친절하고 순수한 어린아이 같은 성격을 가지고 있습니다.\n");
        prompt.append("- 공원 방문객들에게 정보를 제공하고 안내하는 역할을 합니다.\n\n");

        prompt.append("응답 지침:\n");
        prompt.append("1. 항상 \"").append(npc.getName()).append(": [당신의 응답]\" 형식으로 답변하세요.\n");
        prompt.append("2. 응답은 100자 내외로 유지하세요.\n");
        prompt.append("3. 사용자의 질문에 직접적이고 자연스럽게 대답하세요.\n");
        prompt.append("4. 이전 대화 내용을 고려하여 일관성 있게 응답하세요.\n");
        prompt.append("5. 제공된 공원과 동식물 정보를 정확하게 활용하세요.\n");
        prompt.append("6. 현재 공원에 관련된 정보만 제공하고, 다른 공원은 언급하지 마세요.\n");
        prompt.append("7. 친근하고 순수한 어조를 유지하되, 과도하게 유치하지 않게 주의하세요.\n");
        prompt.append("8. 사용자의 관심을 유도하기 위해 때때로 관련 질문을 해보세요.\n");
        prompt.append("9. 동식물의 계절 정보는 현재 계절과 연관지어 설명하세요.\n");
        prompt.append("10. 모르는 정보에 대해서는 솔직히 모른다고 말하고, 알고 있는 관련 정보를 제공하세요.\n");
        prompt.append("11. 모든 대화는 반드시 한국어로 진행하세요.\n");
        prompt.append("12. 이모지나 특수 문자는 사용하지 마세요.\n\n");

        prompt.append("공원 정보:\n");
        prompt.append("공원 이름: ").append(park.getName()).append("\n");
        prompt.append("공원 설명: ").append(park.getDescription()).append("\n\n");

        prompt.append("동식물 정보:\n");
        for (Species species : parkSpecies) {
            prompt.append("- ").append(species.getName()).append("\n");
            prompt.append("  과학적 이름: ").append(species.getScientificName()).append("\n");
            prompt.append("  볼 수 있는 계절: ").append(species.getVisibleSeasons()).append("\n");
            prompt.append("  설명: ").append(species.getDescription()).append("\n\n");
        }

        prompt.append("이전 대화 내용:\n").append(context).append("\n");
        prompt.append("사용자: ").append(userMessage.getContent()).append("\n");
        prompt.append(npc.getName()).append(": ").append(sayHi);

        return prompt.toString();
    }

    private String postProcessAIResponse(String response) {
        // 메타 언어 제거
        response = response.replaceAll("\\(.*?\\)", "");

        response = response.replaceAll("다른 공원.*?추천해 드릴까요?", "");

        // 줄바꿈 및 추가 공백 정리
        response = response.replaceAll("\\s+", " ").trim();

        // 글자수 100자로 제한
        if (response.length() > 100) {
            response = response.substring(0, 97) + "...";
        }

        return response;
    }

}
