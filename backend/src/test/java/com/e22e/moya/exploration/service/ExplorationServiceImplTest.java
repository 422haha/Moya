package com.e22e.moya.exploration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.npc.Npc;
import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.npc.NpcPos;
import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.common.entity.species.ParkSpecies;
import com.e22e.moya.common.entity.species.SpeciesPos;
import com.e22e.moya.exploration.dto.ExplorationStartDto;
import com.e22e.moya.exploration.dto.SpeciesDto;
import com.e22e.moya.exploration.dto.NpcDto;
import com.e22e.moya.exploration.repository.ParkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ExplorationServiceImplTest {

    @Mock
    private ParkRepository parkRepository;

    @InjectMocks
    private ExplorationServiceImpl explorationService;

    private Park mockPark;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory();

        mockPark = new Park();
        mockPark.setId(1L);
        mockPark.setName("싸피 뒷뜰");
        mockPark.setDescription("싸피 구미 캠퍼스 기숙사에 위치한 공원");

        Species species = new Species();
        species.setId(1L);
        species.setName("청설모");
        species.setScientific_name("Sciurus vulgaris");
        species.setDescription("귀여운 다람쥐과의 포유류");
        species.setImageUrl("https://example.com/squirrel.jpg");

        ParkSpecies parkSpecies = new ParkSpecies();
        parkSpecies.setSpecies(species);

        SpeciesPos speciesPos = new SpeciesPos();
        Point point = geometryFactory.createPoint(new Coordinate(128.416000, 36.107000));
        speciesPos.setPos(point);
        parkSpecies.setPositions(Arrays.asList(speciesPos));

        mockPark.setParkSpecies(Arrays.asList(parkSpecies));

        Npc npc = new Npc();
        npc.setId(1L);
        npc.setName("수달");

        ParkNpcs parkNpc = new ParkNpcs();
        parkNpc.setNpc(npc);

        NpcPos npcPos = new NpcPos();
        npcPos.setPos(geometryFactory.createPoint(new Coordinate(128.416000, 36.107000)));
        parkNpc.setPositions(Arrays.asList(npcPos));

        mockPark.setParkNpcs(Arrays.asList(parkNpc));
    }

    @Test
    void 공원조회성공() {
        // Given
        Long parkId = 1L;
        Long userId = 1L;
        when(parkRepository.findById(parkId)).thenReturn(Optional.of(mockPark));

        // When
        ExplorationStartDto result = explorationService.getInitInfo(parkId, userId);

        // Then
        assertNotNull(result);

        System.out.println("====================== species ======================");
        for (SpeciesDto speciesDto : result.getSpecies()) {
            System.out.println("id: " + speciesDto.getId());
            System.out.println("이름: " + speciesDto.getName());
            System.out.println("학명: " + speciesDto.getScientificName());
            System.out.println("설명: " + speciesDto.getDescription());
            System.out.println("이미지: " + speciesDto.getImageUrl());
            System.out.println("위치:");
            speciesDto.getPositions().forEach(pos ->
                System.out.println(" -- 위도: " + pos.getLatitude() + ", 경도: " + pos.getLongitude()));
        }

        System.out.println("====================== npc ======================");
        for (NpcDto npcDto : result.getNpcs()) {
            System.out.println("id: " + npcDto.getId());
            System.out.println("이름: " + npcDto.getName());
            System.out.println("위치:");
            npcDto.getPositions().forEach(pos ->
                System.out.println(" -- 위도: " + pos.getLatitude() + ", 경도: " + pos.getLongitude()));
        }

        assertEquals(1, result.getSpecies().size());
        SpeciesDto speciesDto = result.getSpecies().get(0);
        assertEquals(1L, speciesDto.getId());
        assertEquals("청설모", speciesDto.getName());
        assertEquals("Sciurus vulgaris", speciesDto.getScientificName());
        assertEquals("귀여운 다람쥐과의 포유류", speciesDto.getDescription());
        assertEquals("https://example.com/squirrel.jpg", speciesDto.getImageUrl());
        assertEquals(1, speciesDto.getPositions().size());
        assertEquals(36.107000, speciesDto.getPositions().get(0).getLatitude());
        assertEquals(128.416000, speciesDto.getPositions().get(0).getLongitude());

        assertEquals(1, result.getNpcs().size());
        NpcDto npcDto = result.getNpcs().get(0);
        assertEquals(1L, npcDto.getId());
        assertEquals("수달", npcDto.getName());
        assertEquals(1, npcDto.getPositions().size());
        assertEquals(36.107000, npcDto.getPositions().get(0).getLatitude());
        assertEquals(128.416000, npcDto.getPositions().get(0).getLongitude());

        verify(parkRepository).findById(parkId);
    }
}