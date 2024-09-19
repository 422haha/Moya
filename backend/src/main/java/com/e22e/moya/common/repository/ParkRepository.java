package com.e22e.moya.common.repository;

import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.species.ParkSpecies;
import com.e22e.moya.exploration.dto.info.ParkSpeciesDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkRepository extends JpaRepository<Park, Long> {

    Optional<Park> findById(Long id);

    @Query("SELECT DISTINCT ps FROM Park p JOIN p.parkSpecies ps JOIN FETCH ps.species WHERE p.id=:parkId")
    List<ParkSpecies> findSpeciesInPark(@Param("parkId") Long parkId);

    @Query("SELECT DISTINCT pn FROM Park p JOIN p.parkNpcs pn JOIN FETCH pn.npc JOIN FETCH pn.positions WHERE p.id=:parkId")
    List<ParkNpcs> findNpcsInPark(@Param("parkId") Long parkId);


    @Query(value =
        "SELECT s.species_id as speciesId, s.name as speciesName, " +
            "s.scientific_name as scientificName, s.description as description, " +
            "d.image_url as imageUrl, " +
            "sp.pos as position " +
            "FROM discovery d " +
            "JOIN species s ON d.species_id = s.species_id " +
            "JOIN species_pos sp ON d.species_pos_id = sp.id " +
            "JOIN park_species ps ON sp.park_species_id = ps.id " +
            "WHERE d.user_id=:userId AND ps.park_id=:parkId",
        nativeQuery = true)
    List<ParkSpeciesDto> findMyDiscoveredSpecies(@Param("parkId") Long parkId,
        @Param("userId") Long userId);


    @Query(nativeQuery = true, value =
        "WITH clustered AS ( " +
            "    SELECT " +
            "        s.species_id, s.name, s.scientific_name, s.description, s.image_url, " +
            "        sp.pos, " +
            // DBSCAN 클러스터링을 통해 cluster_id 생성
            "    ST_ClusterDBSCAN(sp.pos, 20, 3) OVER () AS cluster_id " +
            "    FROM park_species ps " +
            "    JOIN species s ON ps.species_id = s.species_id " + // 종 정보 join
            "    JOIN species_pos sp ON ps.id = sp.park_species_id " + // 위치 정보 join
            "    WHERE ps.park_id = :parkId " + // 공원 id로 필터링
            ") " +
            "SELECT " +
            "    c.species_id AS speciesId, " +
            "    c.name AS speciesName, " +
            "    c.scientific_name AS scientificName, " +
            "    c.description, " +
            "    c.image_url AS imageUrl, " +
            "    CASE " +
            // 클러스터 내 포인트 수가 3 이상이면
            "        WHEN COUNT(*) OVER (PARTITION BY c.cluster_id) >= 3 THEN " +
            "            ST_Centroid(ST_Collect(c.pos)) " + // 클러스터의 중심점 계산
            "        ELSE c.pos " + // 단일 포인트 반환
            "    END AS position " +
            "FROM clustered c " +
            "GROUP BY c.species_id, c.name, c.scientific_name, c.description, c.image_url, c.pos, c.cluster_id "
            +
            "ORDER BY c.species_id, c.cluster_id")
    List<ParkSpeciesDto> findAllSpecies(
        @Param("parkId") Long parkId
    );
}
