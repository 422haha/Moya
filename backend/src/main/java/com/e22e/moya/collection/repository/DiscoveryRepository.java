package com.e22e.moya.collection.repository;

import com.e22e.moya.common.entity.Discovery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscoveryRepository extends JpaRepository<Discovery, Long> {
    List<Discovery> findByUserId(Long userId);

    List<Discovery> findByUserIdAndSpeciesId(Long userId, Long speciesId);

    Discovery findTopByUserIdAndSpeciesIdOrderByDiscoveryTimeDesc(Long userId, Long speciesId);
}
