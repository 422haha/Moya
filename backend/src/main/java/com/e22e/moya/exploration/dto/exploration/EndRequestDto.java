package com.e22e.moya.exploration.dto.exploration;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class EndRequestDto {

    //이동 경로
    private List<Points> route;
    // 걸음 수
    private int steps;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Points {

        private double latitude;
        private double longitude;

        public boolean isValid() {
            return latitude >= -90 && latitude <= 90 &&
                longitude >= -180 && longitude <= 180;
        }
    }
}
