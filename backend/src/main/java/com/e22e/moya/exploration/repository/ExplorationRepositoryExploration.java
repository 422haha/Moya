package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.Exploration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExplorationRepositoryExploration extends JpaRepository<Exploration, Long> {

    @Query(value = "SELECT ST_Length(ST_Transform(route, 3857)) FROM exploration WHERE exploration_id = :id", nativeQuery = true)
    Double calculateDistance(@Param("id") Long id);
}
