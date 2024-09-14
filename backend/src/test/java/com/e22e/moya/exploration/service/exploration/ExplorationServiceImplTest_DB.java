package com.e22e.moya.exploration.service.exploration;

import static org.junit.jupiter.api.Assertions.*;

import com.e22e.moya.common.entity.*;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.species.*;
import com.e22e.moya.exploration.dto.exploration.AddRequestDto;
import com.e22e.moya.exploration.dto.exploration.AddResponseDto;
import com.e22e.moya.exploration.repository.*;
import com.e22e.moya.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.locationtech.jts.geom.GeometryFactory;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ExplorationServiceImplTest_DB {

    @Autowired
    private ExplorationService explorationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private ExplorationRepository explorationRepository;

    @Autowired
    private ParkSpeciesRepository parkSpeciesRepository;

    @Autowired
    private SpeciesPosRepository speciesPosRepository;

    @Autowired
    private DiscoveryRepository discoveryRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void setUp() {
        printAllEntities("초기 data.sql 데이터");
    }

    @Test
    void addOnDictionary_기존_종_기존_위치() {
        // Given
        Long userId = 1L;
        Long parkId = 1L;
        Long speciesId = 1L;
        Park park = parkRepository.findById(parkId).orElseThrow();
        Exploration exploration = createAndSaveExploration(userId, park);

        AddRequestDto requestDto = new AddRequestDto();
        requestDto.setSpeciesId(speciesId);
        requestDto.setLatitude(36.107000);
        requestDto.setLongitude(128.416000);
        requestDto.setImageUrl("image.jpg");

        printAllEntities("기존 종, 기존 위치 추가 전 엔티티");

        // When
        AddResponseDto responseDto = explorationService.addOnDictionary(userId, exploration.getId(), requestDto);

        // Then
//        assertNotNull(responseDto);
//        assertEquals(speciesId, responseDto.getSpeciesId());
//        assertEquals("청설모", responseDto.getSpeciesName());

        printAllEntities("기존 종, 기존 위치 추가 이후 엔티티");
    }

    @Test
    void addOnDictionary_공원에_존재하는_종_새로운_위치() {
        // Given
        Long userId = 1L;
        Long parkId = 1L;
        Long speciesId = 1L;
        Park park = parkRepository.findById(parkId).orElseThrow();
        Exploration exploration = createAndSaveExploration(userId, park);

        AddRequestDto requestDto = new AddRequestDto();
        requestDto.setSpeciesId(speciesId);
        requestDto.setLatitude(35.108000);  // 새로운 위치
        requestDto.setLongitude(129.417000);
        requestDto.setImageUrl("image.jpg");

        printAllEntities("새로운 위치 추가하기 전 엔티티");

        // When
        AddResponseDto responseDto = explorationService.addOnDictionary(userId, exploration.getId(), requestDto);

        // Then
//        assertNotNull(responseDto);
//        assertEquals(speciesId, responseDto.getSpeciesId());
//        assertEquals("청설모", responseDto.getSpeciesName());

        printAllEntities("!! == 새로운 위치 추가한 이후 엔티티");
    }

    @Test
    void addOnDictionary_공원에_없던_새로운_종() {
        // Given
        Long userId = 1L;
        Long parkId = 1L;
        Long newSpeciesId = 7L;  // 데이터베이스에 없는 새로운 종 ID
        Park park = parkRepository.findById(parkId).orElseThrow();
        Exploration exploration = createAndSaveExploration(userId, park);
        Species newSpecies = createAndSaveSpecies("딱따구리");

        AddRequestDto requestDto = new AddRequestDto();
        requestDto.setSpeciesId(newSpeciesId);
        requestDto.setLatitude(36.109000);
        requestDto.setLongitude(128.418000);
        requestDto.setImageUrl("딱따구리.jpg");

        printAllEntities("새로운 종 추가하기 전 엔티티");

        // When
        AddResponseDto responseDto = explorationService.addOnDictionary(userId, exploration.getId(), requestDto);

        // Then
//        assertNotNull(responseDto);
//        assertEquals(newSpeciesId, responseDto.getSpeciesId());
//        assertEquals("딱따구리", responseDto.getSpeciesName());

        printAllEntities("!! == 새로운 종 추가한 이후 엔티티");
    }

    private Exploration createAndSaveExploration(Long userId, Park park) {
        Exploration exploration = new Exploration();
        exploration.setUserId(userId);
        exploration.setPark(park);
        exploration.setStartTime(LocalDateTime.now());
        return explorationRepository.save(exploration);
    }

    private Species createAndSaveSpecies(String name) {
        Species species = new Species();
        species.setName(name);
        return speciesRepository.save(species);
    }

    private void printAllEntities(String stage) {
        System.out.println();
        System.out.println("==========" + stage);
        System.out.println("Users:");
        userRepository.findAll().forEach(System.out::println);
        System.out.println();
        System.out.println("Parks:");
        parkRepository.findAll().forEach(System.out::println);
        System.out.println();
        System.out.println("Species:");
        speciesRepository.findAll().forEach(System.out::println);
        System.out.println();
        System.out.println("Explorations:");
        explorationRepository.findAll().forEach(System.out::println);
        System.out.println();
        System.out.println("ParkSpecies:");
        parkSpeciesRepository.findAll().forEach(System.out::println);
        System.out.println();
        System.out.println("SpeciesPos:");
        speciesPosRepository.findAll().forEach(System.out::println);
        System.out.println();
        System.out.println("Discoveries:");
        discoveryRepository.findAll().forEach(System.out::println);
        System.out.println("====================================");
        System.out.println();
    }
}