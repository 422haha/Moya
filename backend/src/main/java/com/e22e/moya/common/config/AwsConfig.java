package com.e22e.moya.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfig {

    // AWS 액세스 키
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    // AWS 시크릿 키
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    // AWS 리전
    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * S3 클라이언트를 생성하고 반환
     *
     * @return  S3 클라이언트 객체
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .build();
    }
}
