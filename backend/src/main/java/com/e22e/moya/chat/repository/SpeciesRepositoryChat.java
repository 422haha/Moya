package com.e22e.moya.chat.repository;

import com.e22e.moya.common.entity.species.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpeciesRepositoryChat extends JpaRepository<Species, Long> {

    @Query("SELECT s FROM Species s " +
        "JOIN s.parkSpecies ps " +
        "JOIN ps.park p " +
        "WHERE p.id = :parkId")
    List<Species> findAllSpeciesByParkId(@Param("parkId") Long parkId);
}

