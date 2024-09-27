package com.e22e.moya.season.repository;

import com.e22e.moya.common.entity.species.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeciesSeasonRepository extends JpaRepository<Species, Long> {

    /**
     * 특정 동식물이 있는 공원의 ID 목록을 조회
     *
     * @param speciesId 동식물 ID
     * @return 해당 동식물이 있는 공원의 ID 목록
     */
    @Query("SELECT ps.park.id FROM ParkSpecies ps WHERE ps.species.id = :speciesId")
    List<Long> findParkIdsBySpeciesId(Long speciesId);
}
