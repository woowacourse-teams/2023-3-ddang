package com.ddang.ddang.image.domain.dto;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.AuctionImage;

public record StoreImageDto(String uploadName, String storeName) {

    public ProfileImage toEntity() {
        return new ProfileImage(uploadName, storeName);
    }

    public AuctionImage toAuctionEntity() {
        return new AuctionImage(uploadName, storeName);
    }
}
