package com.e22e.moya.collection.repository;

import com.e22e.moya.common.entity.species.Species;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeciesRepository extends JpaRepository<Species, Long> {

}
