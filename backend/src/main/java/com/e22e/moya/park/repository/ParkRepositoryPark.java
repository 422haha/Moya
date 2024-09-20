package com.e22e.moya.park.repository;

import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.park.ParkPos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkRepositoryPark extends JpaRepository<Park, Long> {

    @Query(value =
        "SELECT p.* FROM park p " +
            "JOIN park_pos pp ON p.id = pp.park_id " +
            "ORDER BY ST_Distance(CAST(pp.pos AS geography), CAST(ST_SetSRID(ST_MakePoint(?2, ?1), 4326) AS geography)) ASC "
            +
            "LIMIT 1", nativeQuery = true)
    Optional<Park> findNearestPark(double latitude, double longitude);

    @Query(value =
        "SELECT p.*, ST_Distance(CAST(pp.pos AS geography), CAST(ST_SetSRID(ST_MakePoint(?2, ?1), 4326) AS geography)) AS distance "
            +
            "FROM park p " +
            "JOIN park_pos pp ON p.id = pp.park_id " +
            "ORDER BY distance ASC " +
            "LIMIT ?4 OFFSET ?3", nativeQuery = true)
    List<Park> findParksWithDistance(double latitude, double longitude, int offset, int size);

    @Query(value = "SELECT ST_Distance(CAST(e.pos AS geography), " +
        "CAST(ST_SetSRID(ST_MakePoint(?2, ?1), 4326) AS geography)) " +
        "FROM park_pos e WHERE e.park_id = ?1", nativeQuery = true)
    double calculateDistance(Long parkId, double latitude, double longitude);

    @Query(value = "SELECT * FROM park_pos e " +
        "WHERE e.park_id = ?1 " +
        "ORDER BY ST_Distance(CAST(e.pos AS geography), CAST(ST_SetSRID(ST_MakePoint(?2, ?1), 4326) AS geography)) ASC "
        +
        "LIMIT 1", nativeQuery = true)
    Optional<ParkPos> findNearestEntrance(Long parkId, double latitude, double longitude);
}
