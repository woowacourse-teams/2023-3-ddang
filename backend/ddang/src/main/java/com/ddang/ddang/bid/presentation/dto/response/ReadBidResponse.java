package com.ddang.ddang.bid.presentation.dto.response;

import com.ddang.ddang.bid.application.dto.response.ReadBidDto;
import java.time.LocalDateTime;

public record ReadBidResponse(String name, String profileImage, int price, LocalDateTime bidTime) {

    public static ReadBidResponse of(final ReadBidDto dto, final String imageRelativeUrl) {
        return new ReadBidResponse(
                dto.name(),
                imageRelativeUrl + dto.profileImageStoreName(),
                dto.price(),
                dto.bidTime()
        );
    }
}
