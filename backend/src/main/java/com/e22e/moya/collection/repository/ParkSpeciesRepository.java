package com.e22e.moya.collection.repository;

import com.e22e.moya.common.entity.species.ParkSpecies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkSpeciesRepository extends JpaRepository<ParkSpecies, Long> {
    Page<ParkSpecies> findByParkId(Long parkId, Pageable pageable);

    long countByParkId(Long parkId);
}
