package com.ddang.ddang.user.presentation.dto.response;

public record SellerResponse(
        Long id,
        String image,
        String nickname,
        Double reliability
) {
}
