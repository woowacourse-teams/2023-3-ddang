package com.ddang.ddang.bid.presentation.dto.response;

import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReadBidResponse(
        String name,
        String profileImage,
        int price,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime bidTime
) {

    public static ReadBidResponse from(final ReadBidDto dto) {
        return new ReadBidResponse(dto.name(), dto.profileImage(), dto.price(), dto.bidTime());
    }
}
