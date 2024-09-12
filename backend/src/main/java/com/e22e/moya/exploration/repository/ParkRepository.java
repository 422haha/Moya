package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.species.ParkSpecies;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkRepository extends JpaRepository<Park, Long> {

    Optional<Park> findById(Long id);

    @Query("SELECT DISTINCT ps FROM Park p JOIN p.parkSpecies ps JOIN FETCH ps.species WHERE p.id = :parkId")
    List<ParkSpecies> findSpeciesInPark(@Param("parkId") Long parkId);

    @Query("SELECT DISTINCT pn FROM Park p JOIN p.parkNpcs pn JOIN FETCH pn.npc JOIN FETCH pn.positions WHERE p.id = :parkId")
    List<ParkNpcs> findNpcsInPark(@Param("parkId") Long parkId);

}
