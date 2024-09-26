package com.e22e.moya.chatbot;

import com.e22e.moya.common.entity.chatting.Chat;
import com.e22e.moya.common.entity.chatting.Message;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.e22e.moya.chatbot.ChatUtils.glob;
import static com.e22e.moya.chatbot.ChatUtils.toPath;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;

/**
 * ChatService 인터페이스 구현체
 */
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final Assistant assistant; // AI 챗봇과의 상호작용을 관리하는 객체

    /**
     * OpenAI API 키로 Assistant 객체를 초기화
     *
     * @param chatRepository 채팅 데이터 관리 repo
     */
    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;

        // data.sql 로드 및 ContentRetriever 생성
        List<Document> documents = loadDocuments(toPath("./"), glob("*.sql"));
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.from(embeddingStore);

        // Assistant 설정에 ContentRetriever 추가
        this.assistant = AiServices.builder(Assistant.class)
            .chatLanguageModel(OpenAiChatModel.builder()
                .apiKey(ChatUtils.OPENAI_API_KEY)
                .temperature(0.7)  // 응답 민감도 조절
                .build())
            .contentRetriever(contentRetriever)  // 문서 기반 정보 검색 가능하도록 설정
            .build();
    }

    /**
     * 사용자 메시지 처리, AI 응답 생성, DB 저장
     *
     * @param requestDto 사용자 메시지 DTO
     * @param chatId 채팅 ID, 이전 대화 내역을 불러오기 위해 사용
     * @return ChatResponseDto 객체
     */
    @Override
    @Transactional
    public ChatResponseDto processUserMessage(ChatRequestDto requestDto, Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(new Chat()); // 기존 채팅 내역을 불러오거나 새로운 채팅 생성

        // 사용자 메시지 생성 및 추가
        Message userMessage = new Message();
        userMessage.setContent(requestDto.getMessage());
        userMessage.setMessageTime(LocalDateTime.now());
        userMessage.setUserMessage(true);
        chat.getMessages().add(userMessage);

        // RAG 기반 AI 응답 생성
        String response = assistant.answer(requestDto.getMessage());

        // AI 메시지 생성 및 추가
        Message aiMessage = new Message();
        aiMessage.setContent(response);
        aiMessage.setMessageTime(LocalDateTime.now());
        aiMessage.setUserMessage(false);
        chat.getMessages().add(aiMessage);

        // 채팅 기록 저장
        chatRepository.save(chat);

        return new ChatResponseDto(response); // AI 응답 반환
    }
}
