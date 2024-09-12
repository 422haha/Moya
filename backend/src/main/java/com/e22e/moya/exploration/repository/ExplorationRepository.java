package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.Exploration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExplorationRepository extends JpaRepository<Exploration, Long> {

}
