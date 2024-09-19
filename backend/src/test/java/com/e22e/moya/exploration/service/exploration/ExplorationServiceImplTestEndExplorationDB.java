package com.e22e.moya.exploration.service.exploration;

import static org.junit.jupiter.api.Assertions.*;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.exploration.dto.exploration.EndRequestDto;
import com.e22e.moya.exploration.dto.exploration.EndResponseDto;
import com.e22e.moya.common.repository.ExplorationRepository;
import com.e22e.moya.common.repository.ParkRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ExplorationServiceImplTestEndExplorationDB {

    @Autowired
    private ExplorationService explorationService;

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private ExplorationRepository explorationRepository;


    @Test
    void endExploration_정상_종료() {
        // Given
        Long userId = 1L;
        Park park = parkRepository.findById(1L).orElseThrow();
        Exploration exploration = createAndSaveExploration(userId, park);

        EndRequestDto endRequestDto = new EndRequestDto();
        List<EndRequestDto.Points> route = new ArrayList<>();
        route.add(createPoint(36.107442, 128.410590));
        route.add(createPoint(36.107535, 128.417373));
        route.add(createPoint(36.105738, 128.416504));
        endRequestDto.setRoute(route);
        endRequestDto.setSteps(1000);

        printAllEntities("탐험 종료 전 엔티티");

        EndResponseDto responseDto = explorationService.endExploration(userId, exploration.getId(),
            endRequestDto);

        assertNotNull(responseDto);
        assertNotNull(responseDto.getExplorationId());

        Exploration endedExploration = explorationRepository.findById(
            responseDto.getExplorationId()).orElseThrow();
        assertNotNull(endedExploration.getEndTime());
        assertEquals(1000, endedExploration.getSteps());
        assertNotNull(endedExploration.getRoute());
        assertTrue(endedExploration.getDistance() > 0);

        printAllEntities("탐험 종료 후 엔티티");
    }

    private EndRequestDto.Points createPoint(double latitude, double longitude) {
        EndRequestDto.Points point = new EndRequestDto.Points();
        point.setLatitude(latitude);
        point.setLongitude(longitude);
        return point;
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
        System.out.println("====================================");
        System.out.println();
    }

}