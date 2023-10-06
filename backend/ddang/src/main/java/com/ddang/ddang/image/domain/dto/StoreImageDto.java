package com.ddang.ddang.image.domain.dto;

import com.ddang.ddang.image.domain.AuctionImage;

public record StoreImageDto(String uploadName, String storeName) {

    public AuctionImage toEntity() {
        return new AuctionImage(uploadName, storeName);
    }
}
