package com.e22e.moya.park.controller;

import com.e22e.moya.park.dto.ParkDetailResponseDto;
import com.e22e.moya.park.dto.ParkListResponseDto;
import com.e22e.moya.park.dto.ParkResponseDto;
import com.e22e.moya.park.service.ParkService;
// import com.e22e.moya.common.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParkControllerTest {

    @InjectMocks
    private ParkController parkController;

    @Mock
    private ParkService parkService;

//    @Mock
//    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNearestPark() {
        // Given
//        String token = "Bearer mockToken";
        double latitude = 36.107000;
        double longitude = 128.416000;
        Long userId = 1L;

        // setter 방식으로 변경
        ParkResponseDto parkResponseDto = new ParkResponseDto();
        parkResponseDto.setParkId(1L);
        parkResponseDto.setParkName("싸피 뒷뜰");
        parkResponseDto.setDistance(500);
        parkResponseDto.setImageUrl("image_url");

//        when(jwtUtil.getUserIdFromToken(token)).thenReturn(userId);
        when(parkService.getNearestPark(userId, latitude, longitude)).thenReturn(parkResponseDto);

        // 로그 출력 - Given 단계
        System.out.println(
            "Given 단계 - latitude: " + latitude + ", longitude: " + longitude + ", userId: "
                + userId);

        // When
        ResponseEntity<Map<String, Object>> responseEntity = parkController.getNearestPark(latitude,
            longitude);

        // 로그 출력 - When 단계
        System.out.println("When 단계 - Nearest Park 호출 완료");

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("조회 성공", responseBody.get("message"));
        assertEquals(parkResponseDto, responseBody.get("data"));

        // 로그 출력 - Then 단계
        System.out.println("Then 단계 - 응답 상태 코드: " + responseEntity.getStatusCode());
        System.out.println("Then 단계 - 응답 메시지: " + responseBody.get("message"));
        System.out.println("Then 단계 - 공원 데이터: " + responseBody.get("data"));

        verify(parkService, times(1)).getNearestPark(userId, latitude, longitude);
    }

    @Test
    void testGetParks() {
        // Given
//        String token = "Bearer mockToken";
        double latitude = 36.107000;
        double longitude = 128.416000;
        Long userId = 1L;
        int page = 0;
        int size = 10;

        // setter 방식으로 변경
        ParkResponseDto park1 = new ParkResponseDto();
        park1.setParkId(1L);
        park1.setParkName("싸피 뒷뜰");
        park1.setDistance(500);
        park1.setImageUrl("image_url1");

        ParkResponseDto park2 = new ParkResponseDto();
        park2.setParkId(2L);
        park2.setParkName("동락공원");
        park2.setDistance(1000);
        park2.setImageUrl("image_url2");

        List<ParkResponseDto> parkList = Arrays.asList(park1, park2);

        // setter 방식으로 변경
        ParkListResponseDto parkListResponseDto = new ParkListResponseDto();
        parkListResponseDto.setParks(parkList);

//        when(jwtUtil.getUserIdFromToken(token)).thenReturn(userId);
        when(parkService.getParks(userId, latitude, longitude, page, size)).thenReturn(
            parkListResponseDto);

        // 로그 출력 - Given 단계
        System.out.println(
            "Given 단계 - latitude: " + latitude + ", longitude: " + longitude + ", userId: " + userId
                + ", page: " + page + ", size: " + size);

        // When
        ResponseEntity<Map<String, Object>> responseEntity = parkController.getParks(page, size,
            latitude, longitude);

        // 로그 출력 - When 단계
        System.out.println("When 단계 - 공원 목록 호출 완료");

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("공원 목록 조회 성공", responseBody.get("message"));
        assertEquals(parkListResponseDto, responseBody.get("data"));

        // 로그 출력 - Then 단계
        System.out.println("Then 단계 - 응답 상태 코드: " + responseEntity.getStatusCode());
        System.out.println("Then 단계 - 응답 메시지: " + responseBody.get("message"));
        System.out.println("Then 단계 - 공원 목록: " + responseBody.get("data"));

        verify(parkService, times(1)).getParks(userId, latitude, longitude, page, size);
    }

    @Test
    void testGetParkDetail() {
        // Given
//        String token = "Bearer mockToken";
        Long parkId = 1L;
        Long userId = 1L;

        // setter 방식으로 변경
        ParkDetailResponseDto parkDetailResponseDto = new ParkDetailResponseDto();
        parkDetailResponseDto.setParkId(1L);
        parkDetailResponseDto.setName("싸피 뒷뜰");
        parkDetailResponseDto.setDescription("싸피 기숙사 뒷뜰");
        parkDetailResponseDto.setImageUrl("image_url");

//        when(jwtUtil.getUserIdFromToken(token)).thenReturn(userId);
        when(parkService.getParkDetail(parkId)).thenReturn(parkDetailResponseDto);

        // 로그 출력 - Given 단계
        System.out.println("Given 단계 - parkId: " + parkId + ", userId: " + userId);

        // When
        ResponseEntity<Map<String, Object>> responseEntity = parkController.getParkDetail(parkId);

        // 로그 출력 - When 단계
        System.out.println("When 단계 - 공원 상세 정보 호출 완료");

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("공원 상세 정보 조회 성공", responseBody.get("message"));
        assertEquals(parkDetailResponseDto, responseBody.get("data"));

        // 로그 출력 - Then 단계
        System.out.println("Then 단계 - 응답 상태 코드: " + responseEntity.getStatusCode());
        System.out.println("Then 단계 - 응답 메시지: " + responseBody.get("message"));
        System.out.println("Then 단계 - 공원 상세 데이터: " + responseBody.get("data"));

        verify(parkService, times(1)).getParkDetail(parkId);
    }
}
