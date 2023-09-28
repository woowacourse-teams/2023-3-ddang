package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadUserInChatRoomDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.user.presentation.util.NameProcessor;

public record ReadChatPartnerResponse(Long id, String name, String profileImage) {

    public static ReadChatPartnerResponse from(final ReadUserInChatRoomDto dto) {
        final String name = NameProcessor.process(dto.isDeleted(), dto.name());

        return new ReadChatPartnerResponse(
                dto.id(),
                name,
                ImageUrlCalculator.calculateBy(ImageRelativeUrl.USER, dto.profileImageId())
        );
    }
}
