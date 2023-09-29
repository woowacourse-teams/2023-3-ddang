package com.ddang.ddang.notification.application.dto;

import com.ddang.ddang.chat.application.dto.MessageDto;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.notification.domain.NotificationType;
import lombok.NonNull;

public record CreateNotificationDto(
        @NonNull
        NotificationType notificationType,

        @NonNull
        Long targetUserId,

        @NonNull
        String title,

        @NonNull
        String body,

        @NonNull
        String redirectUrl,

        @NonNull
        String image
) {

    public static CreateNotificationDto from(final MessageDto messageDto) {
        return new CreateNotificationDto(
                NotificationType.MESSAGE,
                messageDto.receiver().getId(),
                messageDto.writer().getName(),
                messageDto.contents(),
                calculateRedirectUrl(messageDto.chatRoom().getId()),
                // TODO: 2023/09/29 여기 user의 profile이미지를 반환하는 부분인데 merge하는 과정에서 누락이 된건지 원래 이 상태였던건지 잘 모르겠어서 수정해둔 상태입니다. 확인 한 번만 부탁드리겠습니다.
                ImageUrlCalculator.calculateBy(messageDto.profileImageAbsoluteUrl(), messageDto.receiver().getProfileImage().getId())
        );
    }

    private static String calculateRedirectUrl(final Long id) {
        return "/chattings/" + id;
    }
}
