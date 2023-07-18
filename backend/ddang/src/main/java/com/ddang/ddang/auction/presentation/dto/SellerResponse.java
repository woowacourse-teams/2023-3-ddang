package com.ddang.ddang.auction.presentation.dto;

public record SellerResponse(
        Long id,
        String image,
        String nickname,
        double reliability
) {
}
