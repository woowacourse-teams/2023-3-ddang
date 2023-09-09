package com.ddang.ddang.image.domain.dto;

import com.ddang.ddang.image.domain.Image;
import com.ddang.ddang.image.domain.AuctionImage;

public record StoreImageDto(String uploadName, String storeName) {

    public Image toEntity() {
        return new Image(uploadName, storeName);
    }

    public AuctionImage toAuctionEntity() {
        return new AuctionImage(uploadName, storeName);
    }
}
