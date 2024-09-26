package com.e22e.moya.exploration.util;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 유틸리티 메서드 제공
 * API 키 로드
 */
public class ChatUtils {

    static final Dotenv dotenv = Dotenv.load();
    public static final String OPENAI_API_KEY = dotenv.get("OPENAI_API_KEY");  // .env 파일에서 환경 변수 로드

    /**
     * 주어진 glob 패턴에 맞는 파일 경로를 매칭하기 위한 PathMatcher 객체를 생성
     */
    public static PathMatcher glob(String glob) {
        return FileSystems.getDefault().getPathMatcher("glob:" + glob);
    }

    /**
     * 클래스 경로에서 상대 경로를 받아 Path 객체로 변환
     * 파일 URL을 Path 객체로 변환
     */
    public static Path toPath(String relativePath) {
        try {
            URL fileUrl = ChatUtils.class.getClassLoader().getResource(relativePath);
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}