package com.e22e.moya.common.s3Service;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PresignedUrlService {

    private final S3Service s3Service;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PresignedUrlService(S3Service s3Service, RedisTemplate<String, Object> redisTemplate) {
        this.s3Service = s3Service;
        this.redisTemplate = redisTemplate;
    }

    public String getPresignedUrl(String objectKey) {
        String cacheKey = "presigned_url:" + objectKey;

        // 캐시에서 url 확인
        String cachedUrl = (String) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUrl != null) {
            return cachedUrl;
        }

        // 새 presignedUrl 생성
        String presignedUrl = s3Service.generatePresignedUrl(objectKey);

        if (presignedUrl != null) {
            // 캐시에 2시간 저장
            redisTemplate.opsForValue().set(cacheKey, presignedUrl, Duration.ofMinutes(120)); // 2시간
        }

        return presignedUrl;
    }
}