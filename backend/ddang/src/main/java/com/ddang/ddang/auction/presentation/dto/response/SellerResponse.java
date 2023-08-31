package com.ddang.ddang.auction.presentation.dto.response;

public record SellerResponse(
        Long id,
        String image,
        String nickname,
        double reliability
) {
}
