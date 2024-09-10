package com.e22e.moya.common.service;

import java.net.MalformedURLException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Slf4j
@Service
public class S3Service {

    // S3 클라이언트
    private final S3Client s3Client;

    // S3 버킷 이름
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // S3 클라이언트 주입
    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * S3 버킷에서 파일을 삭제
     *
     * @param fileUrl 삭제할 파일의 S3 URL
     */
    public void deleteFile(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            if (key != null && !key.isEmpty()) {
                s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
                log.info("Successfully deleted file with key: {}", key);
            } else {
                log.warn("Unable to delete file. Invalid URL: {}", fileUrl);
            }
        } catch (Exception e) {
            log.error("Error deleting file from S3: {}", fileUrl, e);
        }
    }

    /**
     * S3 URL에서 키를 추출
     *
     * @param fileUrl S3 URL
     * @return 추출된 키
     */
    private String extractKeyFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            // Remove leading '/' if present
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (MalformedURLException e) {
            log.error("Invalid S3 URL: {}", fileUrl, e);
            return null;
        }
    }

}