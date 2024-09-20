package com.e22e.moya.park.repository;

import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.park.ParkPos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkRepositoryPark extends JpaRepository<Park, Long> {

    @Query(value =
        "SELECT p.* FROM park p " +
            "JOIN park_pos pp ON p.id = pp.park_id " +
            "ORDER BY ST_Distance(CAST(pp.pos AS geography), ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography) ASC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Park> findNearestPark(@Param("latitude") double latitude, @Param("longitude") double longitude);

    @Query(value =
        "SELECT p.*, ST_Distance(CAST(pp.pos AS geography), ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography) AS distance " +
            "FROM park p " +
            "JOIN park_pos pp ON p.id = pp.park_id " +
            "ORDER BY distance ASC " +
            "LIMIT :size OFFSET :page * :size", nativeQuery = true)
    List<Park> findParksWithDistance(@Param("latitude") double latitude,
        @Param("longitude") double longitude,
        @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT ST_Distance(CAST(e.pos AS geography), " +
        "ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography) " +
        "FROM park_pos e WHERE e.park_id = :parkId", nativeQuery = true)
    double calculateDistance(@Param("parkId") Long parkId, @Param("latitude") double latitude,
        @Param("longitude") double longitude);

    @Query(value = "SELECT * FROM park_pos e " +
        "WHERE e.park_id = :parkId " +
        "ORDER BY ST_Distance(CAST(e.pos AS geography), ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography) ASC " +
        "LIMIT 1", nativeQuery = true)
    Optional<ParkPos> findNearestEntrance(@Param("parkId") Long parkId,
        @Param("latitude") double latitude, @Param("longitude") double longitude);
}
