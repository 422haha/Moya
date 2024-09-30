package com.e22e.moya.collection.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserPhotoDto {
    private String imageUrl;
    private LocalDateTime discoveryTime;
}
