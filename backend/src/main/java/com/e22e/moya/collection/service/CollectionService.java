package com.e22e.moya.collection.service;

import com.e22e.moya.collection.dto.CollectionDetailDto;
import com.e22e.moya.collection.dto.CollectionResponseDto;

public interface CollectionService {

    CollectionResponseDto getAllCollections(Long userId, int page, int size, String filter);

    CollectionResponseDto getParkCollections(Long userId, Long parkId, int page, int size, String filter);

    CollectionDetailDto getCollectionDetail(Long userId, Long itemId);

}
