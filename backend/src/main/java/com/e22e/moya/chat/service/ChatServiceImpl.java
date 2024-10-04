package com.e22e.moya.chat.service;

import com.e22e.moya.chat.dto.ChatRequestDto;
import com.e22e.moya.chat.dto.ChatResponseDto;
import com.e22e.moya.chat.repository.NpcPosRepositoryChat;
import com.e22e.moya.chat.repository.ParkRepositoryChat;
import com.e22e.moya.chat.repository.SpeciesRepositoryChat;
import com.e22e.moya.common.entity.chatting.Chat;
import com.e22e.moya.common.entity.chatting.Message;
import com.e22e.moya.common.entity.npc.Npc;
import com.e22e.moya.common.entity.npc.NpcPos;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.common.util.ChatAssistant;
import com.e22e.moya.common.util.ChatUtils;
import com.e22e.moya.chat.repository.ChatRepositoryChat;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.document.Document;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.e22e.moya.common.util.ChatUtils.glob;
import static com.e22e.moya.common.util.ChatUtils.toPath;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;


@Service
public class ChatServiceImpl implements ChatService {

    private final ParkRepositoryChat parkRepository;
    private final NpcPosRepositoryChat npcPosRepository;
    private final SpeciesRepositoryChat speciesRepository;
    private final ChatRepositoryChat chatRepository;
    private final ChatAssistant assistant; // AI 챗봇과의 상호작용을 관리하는 객체
    private final RedisTemplate<String, Object> redisTemplate;

    public ChatServiceImpl(ParkRepositoryChat parkRepository, NpcPosRepositoryChat npcPosRepository,
        SpeciesRepositoryChat speciesRepository,
        ChatRepositoryChat chatRepository,
        RedisTemplate<String, Object> redisTemplate) {
        this.parkRepository = parkRepository;
        this.npcPosRepository = npcPosRepository;
        this.speciesRepository = speciesRepository;
        this.chatRepository = chatRepository;
        this.redisTemplate = redisTemplate;

        // data.sql 로드 및 ContentRetriever 생성
        List<Document> documents = loadDocuments(toPath("./"), glob("*.sql"));
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.from(embeddingStore);

        // Assistant 설정에 ContentRetriever 추가
        this.assistant = AiServices.builder(ChatAssistant.class)
            .chatLanguageModel(OpenAiChatModel.builder()
                .apiKey(ChatUtils.OPENAI_API_KEY)
                .temperature(0.7)  // 응답 민감도 조절
                .build())
            .contentRetriever(contentRetriever)  // 문서 기반 정보 검색 가능하도록 설정
            .build();
    }

    /**
     * 사용자 메시지 처리, AI 응답 생성, DB 저장
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ChatResponseDto processUserMessage(ChatRequestDto requestDto, Long npcPosId,
        Long userId, Long parkId) {
        try {
            Chat chat = this.chatRepository.findByNpcPosIdAndUserId(npcPosId, userId)
                .orElse(new Chat());

            List<Message> recentMessages = getRecentMessages(chat);
            String context = buildContext(recentMessages);

            Message userMessage = createUserMessage(requestDto.getMessage());
            chat.getMessages().add(userMessage);

            Park park = parkRepository.findById(parkId).orElseThrow(() -> new EntityNotFoundException("공원 엔티티 찾을 수 없습니다."));
            List<Species> parkSpecies = getParkSpecies(parkId);
            NpcPos npcPos = npcPosRepository.findById(npcPosId)
                .orElseThrow(() -> new EntityNotFoundException("npc를 찾을 수 없습니다."));
            Npc npc = npcPos.getParkNpc().getNpc();

            String prompt = buildPrompt(context, userMessage, park, npc, parkSpecies);
            String response = this.assistant.answer(prompt);

            Message aiMessage = createAiMessage(response);
            chat.getMessages().add(aiMessage);

            this.chatRepository.save(chat);

            return new ChatResponseDto(response);
        } catch (EntityNotFoundException e) {
            return new ChatResponseDto(
                "적절한 대답을 찾지 못했어요.");
        } catch (Exception e) {
            return new ChatResponseDto(
                "잘 못알아들었어요.");
        }
    }

    private List<Species> getParkSpecies(Long parkId) {
        String cacheKey = "park_species_" + parkId;
        List<Species> parkSpecies = (List<Species>) redisTemplate.opsForValue().get(cacheKey);
        if (parkSpecies == null) {
            parkSpecies = speciesRepository.findAllSpeciesByParkId(parkId);
            redisTemplate.opsForValue().set(cacheKey, parkSpecies, 2, TimeUnit.HOURS);
        }
        return parkSpecies;
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
        List<Species> parkSpecies) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("너의 이름은 : ").append(npc.getName()).append(".\n");
        prompt.append("너의 성격은 : ").append("친절하고 순수한 어린아이같은 성격이야").append("\n");

        for (Species species : parkSpecies) {
            prompt.append("너가 알고 있는 지식은 : ").append(species.toString()).append("\n");
        }

        prompt.append("이전 대화 내용은: \n").append(context);
        prompt.append("User: ").append(userMessage.getContent()).append("\n");
        prompt.append("AI (as ").append(npc.getName()).append("): ");
        return prompt.toString();
    }

}
