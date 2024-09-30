package com.e22e.moya.park.service;

import com.e22e.moya.exploration.service.info.InfoService;
import com.e22e.moya.park.dto.ParkResponseDto;
import com.e22e.moya.park.repository.ParkRepositoryPark;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.jdbc.Sql;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("/data.sql")
class PopularParkServiceImplTest {

    @Autowired
    private InfoService infoService;
    @Autowired
    private PopularParkServiceImpl popularParkService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private Random random;

    private static RedisServer redisServer;


    @BeforeAll
    static void setUpRedis() throws IOException {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterAll
    static void tearDownRedis() {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }

    @BeforeEach
    void setUp() {
        random = new Random(42);
        // redis park_popularity 키 초기화
        stringRedisTemplate.delete("park_popularity");
    }

    @Test
    void testRandomVisitsAndPopularity() {
        try {
            double testLat = 36.108044;
            double testLon = 128.416431;
            int visitCount = 100;

            // 랜덤 방문
            Map<Long, Integer> visitCounts = new HashMap<>();
            for (Long parkId = 1L; parkId <= 24L; parkId++) {
                int rand = random.nextInt(visitCount) + 1;
                for (int i = 1; i < rand; i++) {
                    infoService.getInitInfo(parkId, 1L);
                    visitCounts.put(parkId, visitCounts.getOrDefault(parkId, 0) + 1);
                }
                System.out.println(
                    "================parkId: " + parkId + ", repeated: " + rand);
            }

            List<ParkResponseDto> result = popularParkService.getPopularParksNearby(testLat,
                testLon);

            for (ParkResponseDto park : result) {
                System.out.println(park);
            }

            assertNotNull(result);
            assertTrue(result.size() <= 10);

            // 인기도 순으로 정렬되었는지
            for (int i = 1; i < result.size(); i++) {
                assertTrue(visitCounts.getOrDefault(result.get(i - 1).getParkId(), 0)
                    >= visitCounts.getOrDefault(result.get(i).getParkId(), 0));
            }

            // 각 공원의 방문 횟수 출력
            System.out.println("공원 방문 횟수");
            for (Map.Entry<Long, Integer> entry : visitCounts.entrySet()) {
                System.out.println("Park " + entry.getKey() + ": " + entry.getValue() + " visits");
            }

            // 반환된 인기 공원 목록 출력
            System.out.println();
            System.out.println("인기 공원 반환");
            for (ParkResponseDto park : result) {
                System.out.println(park.getParkName() + " (ID: " + park.getParkId() + "): "
                    + visitCounts.getOrDefault(park.getParkId(), 0) + " visits, Distance: "
                    + park.getDistance() + "m");
            }

            // 거리가 포함되어 있는지 확인
            for (ParkResponseDto park : result) {
                assertTrue(park.getDistance() >= 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("테스트 실패 " + e.getMessage());
        }
    }

}