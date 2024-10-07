package com.e22e.moya.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OAuthLoginRequestDto {

    private String provider;
    private String accessToken;

}
