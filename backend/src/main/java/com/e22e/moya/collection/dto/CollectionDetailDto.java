package com.e22e.moya.collection.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CollectionDetailDto {
    private Long itemId;
    private String speciesName;
    private String description;
    private String imageUrl;
    private LocalDateTime collectedAt;
    private LocationDto location;
    private List<UserPhotoDto> userPhotos;
}
