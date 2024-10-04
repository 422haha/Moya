package com.e22e.moya.collection.service;

import com.e22e.moya.collection.dto.*;
import com.e22e.moya.common.entity.Discovery;
import com.e22e.moya.common.entity.species.ParkSpecies;
import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.collection.repository.DiscoveryRepository;
import com.e22e.moya.collection.repository.ParkSpeciesRepository;
import com.e22e.moya.collection.repository.SpeciesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final SpeciesRepository speciesRepository;
    private final DiscoveryRepository discoveryRepository;
    private final ParkSpeciesRepository parkSpeciesRepository;

    /**
     * 전체 도감 목록 조회
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param filter 완료된/미발견/전체 필터
     * @return 도감 목록과 진행률
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public CollectionResponseDto getAllCollections(Long userId, int page, int size, String filter) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        List<Species> speciesList = speciesRepository.findAll(pageRequest).getContent();
        List<Discovery> discoveries = discoveryRepository.findByUserId(userId);

        List<Long> discoveredSpeciesIds = discoveries.stream()
            .map(discovery -> discovery.getSpecies().getId())
            .collect(Collectors.toList());

        List<SpeciesDto> speciesDtos = speciesList.stream()
            .map(species -> {
                SpeciesDto dto = new SpeciesDto();
                dto.setSpeciesId(species.getId());
                dto.setSpeciesName(species.getName());
                dto.setDiscovered(discoveredSpeciesIds.contains(species.getId()));
                dto.setImageUrl(species.getImageUrl());
                return dto;
            })
            .collect(Collectors.toList());

        // 필터 적용
        speciesDtos = applyFilter(speciesDtos, filter);

        double progress = ((double) discoveredSpeciesIds.size() / speciesRepository.count()) * 100;

        CollectionResponseDto responseDto = new CollectionResponseDto();
        responseDto.setSpecies(speciesDtos);
        responseDto.setProgress(progress);

        return responseDto;
    }

    /**
     * 특정 공원의 도감 목록 조회
     * @param userId 사용자 ID
     * @param parkId 공원 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param filter 완료된/미발견/전체 필터
     * @return 공원의 도감 목록과 진행률
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public CollectionResponseDto getParkCollections(Long userId, Long parkId, int page, int size, String filter) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        List<ParkSpecies> parkSpeciesList = parkSpeciesRepository.findByParkId(parkId, pageRequest).getContent();
        List<Species> speciesList = parkSpeciesList.stream()
            .map(ParkSpecies::getSpecies)
            .collect(Collectors.toList());

        List<Discovery> discoveries = discoveryRepository.findByUserId(userId);
        List<Long> discoveredSpeciesIds = discoveries.stream()
            .map(discovery -> discovery.getSpecies().getId())
            .collect(Collectors.toList());

        List<SpeciesDto> speciesDtos = speciesList.stream()
            .map(species -> {
                SpeciesDto dto = new SpeciesDto();
                dto.setSpeciesId(species.getId());
                dto.setSpeciesName(species.getName());
                dto.setDiscovered(discoveredSpeciesIds.contains(species.getId()));
                dto.setImageUrl(species.getImageUrl());
                return dto;
            })
            .collect(Collectors.toList());

        // 필터 적용
        List<SpeciesDto> filteredSpeciesDtos = applyFilter(speciesDtos, filter);

        long totalSpecies = parkSpeciesRepository.countByParkId(parkId);
        long discoveredCount = speciesDtos.stream().filter(SpeciesDto::getDiscovered).count();
        double progress = totalSpecies > 0 ? ((double) discoveredCount / totalSpecies) * 100 : 0;

        CollectionResponseDto responseDto = new CollectionResponseDto();
        responseDto.setSpecies(filteredSpeciesDtos);
        responseDto.setProgress(progress);

        return responseDto;
    }

    /**
     * 도감 상세 정보 조회
     * @param userId 사용자 ID
     * @param itemId 도감 ID
     * @return 도감 상세 정보
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public CollectionDetailDto getCollectionDetail(Long userId, Long itemId) {
        Species species = speciesRepository.findById(itemId)
            .orElseThrow(() -> new EntityNotFoundException("해당 종을 찾을 수 없습니다."));

        Discovery discovery = discoveryRepository.findTopByUserIdAndSpeciesIdOrderByDiscoveryTimeDesc(userId, itemId);

        CollectionDetailDto detailDto = new CollectionDetailDto();
        detailDto.setItemId(species.getId());
        detailDto.setSpeciesName(species.getName());
        detailDto.setDescription(species.getDescription());
        detailDto.setImageUrl(species.getImageUrl());

        if (discovery != null) {
            detailDto.setCollectedAt(discovery.getDiscoveryTime());
            LocationDto location = new LocationDto();
            location.setLatitude(discovery.getSpeciesPos().getPos().getPosition().getLat());
            location.setLongitude(discovery.getSpeciesPos().getPos().getPosition().getLon());
            detailDto.setLocation(location);
        }

        // 수집된 사진들 설정
        List<Discovery> userDiscoveries = discoveryRepository.findByUserIdAndSpeciesId(userId, itemId);
        List<UserPhotoDto> userPhotos = userDiscoveries.stream()
            .map(d -> {
                UserPhotoDto photoDto = new UserPhotoDto();
                photoDto.setImageUrl(d.getImageUrl());
                photoDto.setDiscoveryTime(d.getDiscoveryTime());
                return photoDto;
            })
            .collect(Collectors.toList());
        detailDto.setUserPhotos(userPhotos);

        return detailDto;
    }

    /**
     * 필터에 따라 도감 목록을 필터링
     * @param speciesDtos 도감 목록
     * @param filter 필터 종류
     * @return 필터링된 도감 목록
     */
    private List<SpeciesDto> applyFilter(List<SpeciesDto> speciesDtos, String filter) {
        switch (filter) {
            case "completed":
                return speciesDtos.stream()
                    .filter(SpeciesDto::getDiscovered)
                    .collect(Collectors.toList());
            case "undiscovered":
                return speciesDtos.stream()
                    .filter(species -> !species.getDiscovered())
                    .collect(Collectors.toList());
            default:
                return speciesDtos;
        }
    }
}
