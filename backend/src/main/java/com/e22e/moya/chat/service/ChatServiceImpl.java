package com.e22e.moya.chat.service;

import com.e22e.moya.common.entity.chatting.Chat;
import com.e22e.moya.common.entity.chatting.Message;
import com.e22e.moya.common.entity.npc.Npc;
import com.e22e.moya.common.util.ChatAssistant;
import com.e22e.moya.common.util.ChatUtils;
import com.e22e.moya.exploration.dto.chat.ChatRequestDto;
import com.e22e.moya.exploration.dto.chat.ChatResponseDto;
import com.e22e.moya.exploration.repository.ChatRepository;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.document.Document;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

//    private final ChatRepository chatRepository;
//    private final ChatAssistant assistant; // AI 챗봇과의 상호작용을 관리하는 객체
//
//    public ChatServiceImpl(ChatRepository chatRepository) {
//        this.chatRepository = chatRepository;
//
//        // data.sql 로드 및 ContentRetriever 생성
//        List<Document> documents = loadDocuments(toPath("./"), glob("*.sql"));
//        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
//        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
//        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.from(embeddingStore);
//
//        // Assistant 설정에 ContentRetriever 추가
//        this.assistant = AiServices.builder(ChatAssistant.class)
//            .chatLanguageModel(OpenAiChatModel.builder()
//                .apiKey(ChatUtils.OPENAI_API_KEY)
//                .temperature(0.7)  // 응답 민감도 조절
//                .build())
//            .contentRetriever(contentRetriever)  // 문서 기반 정보 검색 가능하도록 설정
//            .build();
//    }
//
//    /**
//     * 사용자 메시지 처리, AI 응답 생성, DB 저장
//     *
//     * @param requestDto 사용자 메시지 DTO
//     * @param chatId     채팅 ID, 이전 대화 내역을 불러오기 위해 사용
//     * @return ChatResponseDto
//     */
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED)
//    public ChatResponseDto processUserMessage(ChatRequestDto requestDto, Long npcPosId,
//        Long userId) {
//        Chat chat = this.chatRepository.findByNpcPosIdAndUserId(npcPosId, userId)
//            .orElse(new Chat());
//
//        List<Message> recentMessages = getRecentMessages(chat);
//        String context = buildContext(recentMessages);
//
//        Message userMessage = createUserMessage(requestDto.getMessage());
//        chat.getMessages().add(userMessage);
//
//        String prompt = context + "User: " + requestDto.getMessage() + "\nAI:";
//        String response = this.assistant.answer(prompt);
//
//        Message aiMessage = createAiMessage(response);
//        chat.getMessages().add(aiMessage);
//
//        this.chatRepository.save(chat);
//
//        return new ChatResponseDto(response);
//    }
//
//    private List<Message> getRecentMessages(Chat chat) {
//        List<Message> messages = new ArrayList<>(chat.getMessages());
//        Collections.sort(messages, new Comparator<Message>() {
//            @Override
//            public int compare(Message m1, Message m2) {
//                return m2.getMessageTime().compareTo(m1.getMessageTime());
//            }
//        });
//        return messages.subList(0, Math.min(messages.size(), 20));
//    }
//
//    private String buildContext(List<Message> messages) {
//        StringBuilder context = new StringBuilder();
//        for (Message message : messages) {
//            context.append(message.isUserMessage() ? "User: " : "AI: ")
//                .append(message.getContent())
//                .append("\n");
//        }
//        return context.toString();
//    }
//
//    private Message createUserMessage(String content) {
//        Message message = new Message();
//        message.setContent(content);
//        message.setMessageTime(LocalDateTime.now());
//        message.setUserMessage(true);
//        return message;
//    }
//
//    private Message createAiMessage(String content) {
//        Message message = new Message();
//        message.setContent(content);
//        message.setMessageTime(LocalDateTime.now());
//        message.setUserMessage(false);
//        return message;
//    }
//
//    private String setPersonality(String context, String userMessage, Npc npc) {
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("You are an NPC named ").append(npc.getName()).append(".\n");
//        prompt.append("Your personality: ").append("friendly").append("\n");
////        prompt.append("Your knowledge base: ").append(npc.getKnowledgeBase()).append("\n");
//        prompt.append("Previous conversation:\n").append(context);
//        prompt.append("User: ").append(userMessage).append("\n");
//        prompt.append("AI (as ").append(npc.getName()).append("): ");
//        return prompt.toString();
//    }

}
