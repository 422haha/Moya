package com.e22e.moya.park.service;

import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PopularParkServiceImpl implements PopularParkService {


    private final StringRedisTemplate stringRedisTemplate;

    public PopularParkServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 공원 인기도 증가 메서드
     *
     * @param parkId 인기도 증가할 공원 id
     */
    @Override
    public void incrementParkPopularity(Long parkId) {
        String key = "park_popularity:" + parkId;
        // 공원 인기도 점수 증가
        Double score = stringRedisTemplate.opsForZSet()
            .incrementScore("popular_parks", parkId.toString(), 1.0);

        if (score != null && score == 1.0) {  // 새로 추가된 공원이라면
            stringRedisTemplate.expire(key, 7, TimeUnit.DAYS);
        }
    }

}
