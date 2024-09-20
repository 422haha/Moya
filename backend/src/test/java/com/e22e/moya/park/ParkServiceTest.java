package com.e22e.moya.park;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.park.dto.ParkListResponseDto;
import com.e22e.moya.park.dto.ParkResponseDto;
import com.e22e.moya.park.repository.ParkRepositoryPark;
import com.e22e.moya.park.service.ParkServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class ParkServiceTest {

    @InjectMocks
    private ParkServiceImpl parkService;  // ParkServiceImpl을 사용해야 함

    @Mock
    private ParkRepositoryPark parkRepositoryPark;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetParks() {
        // Given
        long userId = 1L;
        double latitude = 36.107000;
        double longitude = 128.416000;
        int page = 0;
        int size = 10;

        System.out.println("Given 단계 - 공원 데이터 준비");

        Park park1 = new Park();
        park1.setId(1L);
        park1.setName("싸피 뒷뜰");
        park1.setDescription("싸피 구미 캠퍼스 기숙사에 위치한 공원");

        Park park2 = new Park();
        park2.setId(2L);
        park2.setName("동락공원");
        park2.setDescription("아름다운 낙동강을 끼고 있는 도심 속 공원");

        List<Park> parks = Arrays.asList(park1, park2);
        when(parkRepositoryPark.findParksWithDistance(latitude, longitude, page, size)).thenReturn(parks);

        System.out.println("When 단계 - 공원 목록 가져오기 실행");

        // When
        ParkListResponseDto parkListResponseDto = parkService.getParks(userId, latitude, longitude, page, size);

        System.out.println("Then 단계 - 테스트 결과 확인");
        System.out.println("가져온 공원 목록 크기: " + parkListResponseDto.getParks().size());

        // Then
        assertNotNull(parkListResponseDto);
        assertEquals(2, parkListResponseDto.getParks().size());

        ParkResponseDto responsePark1 = parkListResponseDto.getParks().get(0);
        System.out.println("첫 번째 공원 이름: " + responsePark1.getParkName());
        assertEquals(park1.getId(), responsePark1.getParkId());
        assertEquals(park1.getName(), responsePark1.getParkName());

        ParkResponseDto responsePark2 = parkListResponseDto.getParks().get(1);
        System.out.println("두 번째 공원 이름: " + responsePark2.getParkName());
        assertEquals(park2.getId(), responsePark2.getParkId());
        assertEquals(park2.getName(), responsePark2.getParkName());
    }
}
