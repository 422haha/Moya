package com.e22e.moya.exploration.service.quest;

import static org.junit.jupiter.api.Assertions.*;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.quest.QuestCompleted;
import com.e22e.moya.exploration.dto.quest.list.QuestListResponseDto;
import com.e22e.moya.common.repository.ExplorationRepository;
import com.e22e.moya.common.repository.ParkRepository;
import com.e22e.moya.common.repository.QuestCompletedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class QuestServiceTestCreateAndGet {

    @Autowired
    private QuestService questService;

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private ExplorationRepository explorationRepository;

    @Autowired
    private QuestCompletedRepository questCompletedRepository;

    private Long userId;
    private Exploration exploration;

    @BeforeEach
    void setUp() {
        userId = 1L;
        Park park = parkRepository.findById(1L).orElseThrow();
        exploration = createAndSaveExploration(userId, park);
    }

    @Test
    void testQuestGenerationAndRetrieval() {
        // Given
        printAllEntities("탐험 시작 전 엔티티");

        // When
        questService.generateNewQuests(exploration);

        // Then
        printAllEntities("퀘스트 생성 후 엔티티");

        List<QuestCompleted> generatedQuests = questCompletedRepository.findByExplorationUserIdAndExplorationId(userId, exploration.getId());
        assertFalse(generatedQuests.isEmpty(), "생성된 퀘스트 없음");
        assertTrue(generatedQuests.size() <= 3, "생성된 퀘스트 수가 3개 초과");

        // When
        QuestListResponseDto questListResponseDto = questService.getQuestList(userId, exploration.getId());

        // Then
        assertNotNull(questListResponseDto, "퀘스트 목록 응답이 null.");
        assertEquals(generatedQuests.size(), questListResponseDto.getQuest().size(), "생성된 퀘스트 수와 조회된 퀘스트 수가 불일치함");

        questListResponseDto.getQuest().forEach(questDto -> {
            assertNotNull(questDto.getQuestId(), "퀘스트 Id가 nul.");
            assertNotNull(questDto.getNpcId(), "NPC Id가 null.");
            assertNotNull(questDto.getNpcName(), "NPC 이름이 null.");
            assertTrue(questDto.getLongitude() != 0, "경도가 0.");
            assertTrue(questDto.getLatitude() != 0, "위도가 0");
            assertTrue(questDto.getQuestType() > 0 && questDto.getQuestType() <= 3, "퀘스트 타입이 유효하지 않음.");
            assertNotNull(questDto.getSpeciesId(), "종 Id가 null.");
            assertNotNull(questDto.getSpeciesName(), "종 이름이 null.");
            assertFalse(questDto.isCompleted(), "새로 생성된 퀘스트가 이미 완료 상태입니다.");
        });

        printAllEntities("퀘스트 조회 후 엔티티");
    }

    private Exploration createAndSaveExploration(Long userId, Park park) {
        Exploration exploration = new Exploration();
        exploration.setUserId(userId);
        exploration.setPark(park);
        exploration.setStartTime(LocalDateTime.now());
        return explorationRepository.save(exploration);
    }

    private void printAllEntities(String stage) {
        System.out.println();
        System.out.println("==========" + stage);
        System.out.println("Explorations:");
        explorationRepository.findAll().forEach(System.out::println);
        System.out.println("Quests:");
        questCompletedRepository.findAll().forEach(System.out::println);
        System.out.println("====================================");
        System.out.println();
    }
}

