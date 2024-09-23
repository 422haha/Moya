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

    /**
     * PostGIS를 사용하여 가장 가까운 공원 조회
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return 가장 가까운 공원의 정보
     */
    @Query(value =
        "SELECT p.id AS id, p.name AS name, p.image_url AS imageUrl, " +
            "MIN(ST_Distance(CAST(pp.pos AS geography), CAST(ST_SetSRID(ST_MakePoint(?2, ?1), 4326) AS geography))) AS distance "
            +
            "FROM park p " +
            "JOIN park_pos pp ON p.id = pp.park_id " +
            "GROUP BY p.id, p.name, p.image_url " +
            "ORDER BY distance ASC " +
            "LIMIT 1", nativeQuery = true)
    ParkDistanceProjection findNearestPark(double latitude, double longitude);

    /**
     * PostGIS를 사용하여 공원 목록을 거리 기준으로 조회
     *
     * @param latitude  위도
     * @param longitude 경도
     * @param offset    페이지 오프셋
     * @param size      페이지 크기
     * @return 거리와 함께 정렬된 공원의 리스트
     */
    @Query(value =
        "SELECT p.id AS id, p.name AS name, p.image_url AS imageUrl, " +
            "MIN(ST_Distance(CAST(pp.pos AS geography), CAST(ST_SetSRID(ST_MakePoint(?2, ?1), 4326) AS geography))) AS distance "
            +
            "FROM park p " +
            "JOIN park_pos pp ON p.id = pp.park_id " +
            "GROUP BY p.id, p.name, p.image_url " +
            "ORDER BY distance ASC " +
            "LIMIT ?4 OFFSET ?3", nativeQuery = true)
    List<ParkDistanceProjection> findParksWithDistance(double latitude, double longitude,
        int offset, int size);

    /**
     * 주어진 공원의 위치와 사용자 위치 간의 최소 거리 계산
     *
     * @param parkId    공원 ID
     * @param latitude  사용자 위도
     * @param longitude 사용자 경도
     * @return 최소 거리 값
     */
    @Query(value = "SELECT MIN(ST_Distance(CAST(pp.pos AS geography), " +
        "CAST(ST_SetSRID(ST_MakePoint(?3, ?2), 4326) AS geography))) " +
        "FROM park_pos pp WHERE pp.park_id = ?1", nativeQuery = true)
    Double calculateDistance(Long parkId, double latitude, double longitude);

    /**
     * 주어진 공원의 가장 가까운 출입구 조회
     *
     * @param parkId    공원 ID
     * @param latitude  사용자 위도
     * @param longitude 사용자 경도
     * @return 가장 가까운 출입구 정보
     */
    @Query(value = "SELECT * FROM park_pos pp " +
        "WHERE pp.park_id = ?1 " +
        "ORDER BY ST_Distance(CAST(pp.pos AS geography), CAST(ST_SetSRID(ST_MakePoint(?3, ?2), 4326) AS geography)) ASC "
        +
        "LIMIT 1", nativeQuery = true)
    Optional<ParkPos> findNearestEntrance(Long parkId, double latitude, double longitude);
}
