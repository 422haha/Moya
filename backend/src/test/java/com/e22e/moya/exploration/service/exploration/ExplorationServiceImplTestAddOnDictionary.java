package com.e22e.moya.exploration.service.exploration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.e22e.moya.common.entity.*;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.species.*;
import com.e22e.moya.exploration.repository.DiscoveryRepositoryExploration;
import com.e22e.moya.exploration.repository.ExplorationRepositoryExploration;
import com.e22e.moya.exploration.repository.ParkSpeciesRepositoryExploration;
import com.e22e.moya.exploration.repository.SpeciesPosRepositoryExploration;
import com.e22e.moya.exploration.repository.SpeciesRepositoryExploration;
import com.e22e.moya.exploration.dto.exploration.AddRequestDto;
import com.e22e.moya.exploration.dto.exploration.AddResponseDto;
import com.e22e.moya.user.repository.UserRepository;
import java.util.Optional;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ExplorationServiceImplTestAddOnDictionary {

    @InjectMocks
    private ExplorationServiceImpl explorationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private SpeciesRepositoryExploration speciesRepository;
    @Mock
    private DiscoveryRepositoryExploration discoveryRepository;
    @Mock
    private ExplorationRepositoryExploration explorationRepository;
    @Mock
    private ParkSpeciesRepositoryExploration parkSpeciesRepository;
    @Mock
    private SpeciesPosRepositoryExploration speciesPosRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //기존 위치
    @Test
    void addOnDictionary_기존_위치() {
        // Given
        long userId = 1L;
        Long explorationId = 1L;
        AddRequestDto requestDto = new AddRequestDto();
        requestDto.setSpeciesId(1L);
        requestDto.setLatitude(36.107000);
        requestDto.setLongitude(128.416000);
        requestDto.setImageUrl("image_url");

        Users user = new Users();
        user.setId(userId);

        Species species = new Species();
        species.setId(1L);
        species.setName("청설모");

        Exploration exploration = new Exploration();
        exploration.setId(explorationId);

        Park park = new Park();
        park.setId(1L);
        exploration.setPark(park);

        ParkSpecies parkSpecies = new ParkSpecies();
        parkSpecies.setId(1L);
        parkSpecies.setPark(park);
        parkSpecies.setSpecies(species);

        Point<G2D> location = DSL.point(CoordinateReferenceSystems.WGS84, DSL.g(requestDto.getLongitude(), requestDto.getLatitude()));
        SpeciesPos speciesPos = new SpeciesPos();
        speciesPos.setId(1L);
        speciesPos.setParkSpecies(parkSpecies);
        speciesPos.setPos(location);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(speciesRepository.findById(requestDto.getSpeciesId())).thenReturn(Optional.of(species));
        when(explorationRepository.findById(explorationId)).thenReturn(Optional.of(exploration));
        when(parkSpeciesRepository.findByParkAndSpecies(park, species)).thenReturn(Optional.of(parkSpecies));
        when(speciesPosRepository.findByParkSpeciesAndPos(parkSpecies, location)).thenReturn(Optional.of(speciesPos));
        when(discoveryRepository.save(any(Discovery.class))).thenAnswer(i -> i.getArguments()[0]);

        System.out.println("기존 위치 입력");
        System.out.println("userId: " + userId);
        System.out.println("explorationId: " + explorationId);
        System.out.println("SpeciesId: " + requestDto.getSpeciesId());
        System.out.println("Latitude: " + requestDto.getLatitude());
        System.out.println("Longitude: " + requestDto.getLongitude());
        System.out.println("ImageUrl: " + requestDto.getImageUrl());
        System.out.println("SpeciesName: " + species.getName());
        System.out.println("ParkID: " + park.getId());

        // When
        AddResponseDto responseDto = explorationService.addOnDictionary(userId, explorationId, requestDto);

        System.out.println("출력");
        System.out.println("Response speciesId: " + responseDto.getSpeciesId());
        System.out.println("Response speciesName: " + responseDto.getSpeciesName());
        System.out.println();

        // Then
        assertNotNull(responseDto);
        assertEquals(species.getId(), responseDto.getSpeciesId());
        assertEquals(species.getName(), responseDto.getSpeciesName());
    }

    //새로운 위치
    @Test
    void addOnDictionary_새로운_위치() {
        // Given
        long userId = 1L;
        Long explorationId = 1L;
        AddRequestDto requestDto = new AddRequestDto();
        requestDto.setSpeciesId(1L);
        requestDto.setLatitude(36.107000);
        requestDto.setLongitude(128.416000);
        requestDto.setImageUrl("image_url");

        Users user = new Users();
        user.setId(userId);

        Species species = new Species();
        species.setId(1L);
        species.setName("청설모");

        Exploration exploration = new Exploration();
        exploration.setId(explorationId);

        Park park = new Park();
        park.setId(1L);
        exploration.setPark(park);

        ParkSpecies parkSpecies = new ParkSpecies();
        parkSpecies.setId(1L);
        parkSpecies.setPark(park);
        parkSpecies.setSpecies(species);

        Point<G2D> location = DSL.point(CoordinateReferenceSystems.WGS84, DSL.g(requestDto.getLongitude(), requestDto.getLatitude()));
        SpeciesPos newSpeciesPos = new SpeciesPos();
        newSpeciesPos.setId(2L);
        newSpeciesPos.setParkSpecies(parkSpecies);
        newSpeciesPos.setPos(location);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(speciesRepository.findById(requestDto.getSpeciesId())).thenReturn(Optional.of(species));
        when(explorationRepository.findById(explorationId)).thenReturn(Optional.of(exploration));
        when(parkSpeciesRepository.findByParkAndSpecies(park, species)).thenReturn(Optional.of(parkSpecies));
        when(speciesPosRepository.findByParkSpeciesAndPos(parkSpecies, location)).thenReturn(Optional.empty());
        when(speciesPosRepository.save(any(SpeciesPos.class))).thenReturn(newSpeciesPos);
        when(discoveryRepository.save(any(Discovery.class))).thenAnswer(i -> i.getArguments()[0]);

        System.out.println("새로운 위치 입력");
        System.out.println("userId: " + userId);
        System.out.println("explorationId: " + explorationId);
        System.out.println("SpeciesId: " + requestDto.getSpeciesId());
        System.out.println("Latitude: " + requestDto.getLatitude());
        System.out.println("Longitude: " + requestDto.getLongitude());
        System.out.println("ImageUrl: " + requestDto.getImageUrl());
        System.out.println("SpeciesName: " + species.getName());
        System.out.println("ParkID: " + park.getId());

        // When
        AddResponseDto responseDto = explorationService.addOnDictionary(userId, explorationId, requestDto);

        System.out.println("출력");
        System.out.println("Response speciesId: " + responseDto.getSpeciesId());
        System.out.println("Response speciesName: " + responseDto.getSpeciesName());
        System.out.println();

        // Then
        assertNotNull(responseDto);
        assertEquals(species.getId(), responseDto.getSpeciesId());
        assertEquals(species.getName(), responseDto.getSpeciesName());
    }

    // 새로운 종
    @Test
    void addOnDictionary_새로운_종() {
        long userId = 1L;
        Long explorationId = 1L;
        AddRequestDto requestDto = new AddRequestDto();
        requestDto.setSpeciesId(2L);
        requestDto.setLatitude(36.107000);
        requestDto.setLongitude(128.416000);
        requestDto.setImageUrl("image_url");

        Users user = new Users();
        user.setId(userId);

        Species species = new Species();
        species.setId(2L);
        species.setName("딱따구리?");

        Exploration exploration = new Exploration();
        exploration.setId(explorationId);

        Park park = new Park();
        park.setId(1L);
        exploration.setPark(park);

        ParkSpecies newParkSpecies = new ParkSpecies();
        newParkSpecies.setId(2L);
        newParkSpecies.setPark(park);
        newParkSpecies.setSpecies(species);

        Point<G2D> location = DSL.point(CoordinateReferenceSystems.WGS84, DSL.g(requestDto.getLongitude(), requestDto.getLatitude()));
        SpeciesPos newSpeciesPos = new SpeciesPos();
        newSpeciesPos.setId(3L);
        newSpeciesPos.setParkSpecies(newParkSpecies);
        newSpeciesPos.setPos(location);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(speciesRepository.findById(requestDto.getSpeciesId())).thenReturn(Optional.of(species));
        when(explorationRepository.findById(explorationId)).thenReturn(Optional.of(exploration));
        when(parkSpeciesRepository.findByParkAndSpecies(park, species)).thenReturn(Optional.empty());
        when(parkSpeciesRepository.save(any(ParkSpecies.class))).thenReturn(newParkSpecies);
        when(speciesPosRepository.findByParkSpeciesAndPos(newParkSpecies, location)).thenReturn(Optional.empty());
        when(speciesPosRepository.save(any(SpeciesPos.class))).thenReturn(newSpeciesPos);
        when(discoveryRepository.save(any(Discovery.class))).thenAnswer(i -> i.getArguments()[0]);
        
        System.out.println("새로운 종 입력");
        System.out.println("userId: " + userId);
        System.out.println("explorationId: " + explorationId);
        System.out.println("SpeciesId: " + requestDto.getSpeciesId());
        System.out.println("Latitude: " + requestDto.getLatitude());
        System.out.println("Longitude: " + requestDto.getLongitude());
        System.out.println("ImageUrl: " + requestDto.getImageUrl());
        System.out.println("SpeciesName: " + species.getName());
        System.out.println("ParkID: " + park.getId());

        // When
        AddResponseDto responseDto = explorationService.addOnDictionary(userId, explorationId, requestDto);

        System.out.println("출력");
        System.out.println("Response speciesId: " + responseDto.getSpeciesId());
        System.out.println("Response speciesName: " + responseDto.getSpeciesName());
        System.out.println();

        // Then
        assertNotNull(responseDto);
        assertEquals(species.getId(), responseDto.getSpeciesId());
        assertEquals(species.getName(), responseDto.getSpeciesName());
    }
}