package com.e22e.moya.park.repository;

import com.e22e.moya.common.entity.park.Park;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkRepositoryPark extends JpaRepository<Park, Long> {

    @Query(value = "SELECT p.* FROM park p " +
        "JOIN park_pos pp ON p.id = pp.park_id " +
        "ORDER BY ST_Distance(pp.pos, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) ASC " +
        "LIMIT 1",
        nativeQuery = true)
    Optional<Park> findNearestPark(@Param("latitude") double latitude,
        @Param("longitude") double longitude);

    @Query(value =
        "SELECT p.*, ST_Distance(pp.pos, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) AS distance "
            +
            "FROM park p " +
            "JOIN park_pos pp ON p.id = pp.park_id " +
            "ORDER BY distance ASC " +
            "LIMIT :size OFFSET :page * :size",
        nativeQuery = true)
    List<Park> findParksWithDistance(@Param("latitude") double latitude,
        @Param("longitude") double longitude,
        @Param("page") int page, @Param("size") int size);

    @Query("SELECT ST_Distance(e.pos, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) " +
        "FROM Park p JOIN p.entrances e WHERE p.id = :parkId")
    double calculateDistance(@Param("longitude") double longitude,
        @Param("latitude") double latitude, @Param("parkId") Long parkId);
}
