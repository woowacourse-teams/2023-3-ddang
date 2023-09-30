package com.ddang.ddang.bid.presentation.dto.response;

import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.user.presentation.util.NameProcessor;
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
        final String name = NameProcessor.process(dto.isDeletedUser(), dto.name());
        return new ReadBidResponse(
                name,
                ImageUrlCalculator.calculateBy(ImageRelativeUrl.USER, dto.profileImageId()),
                dto.price(),
                dto.bidTime()
        );
    }
}
