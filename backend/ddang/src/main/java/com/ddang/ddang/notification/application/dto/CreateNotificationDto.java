package com.ddang.ddang.notification.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.dto.AuctionAndImageDto;
import com.ddang.ddang.chat.application.dto.MessageDto;
import com.ddang.ddang.image.domain.AuctionImage;
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

    private static final String BID_NOTIFICATION_MESSAGE_FORMAT = "상위 입찰자가 나타났습니다. 구매를 원하신다면 더 높은 가격을 제시해 주세요.";

    public static CreateNotificationDto from(final MessageDto messageDto) {
        return new CreateNotificationDto(
                NotificationType.MESSAGE,
                messageDto.receiver().getId(),
                messageDto.writer().getName(),
                messageDto.contents(),
                calculateChatRoomRedirectUrl(messageDto.chatRoom().getId()),
                ImageUrlCalculator.calculateBy(messageDto.profileImageAbsoluteUrl(), messageDto.writer()
                                                                                               .getProfileImage()
                                                                                               .getId())
        );
    }

    public static CreateNotificationDto of(final Long previousBidderId, final AuctionAndImageDto auctionAndImageDto, final String auctionImageAbsoluteUrl) {
        final Auction auction = auctionAndImageDto.auction();
        final AuctionImage auctionImage = auctionAndImageDto.auctionImage();

        return new CreateNotificationDto(
                NotificationType.BID,
                previousBidderId,
                auction.getTitle(),
                BID_NOTIFICATION_MESSAGE_FORMAT,
                calculateAuctionRedirectUrl(auction.getId()),
                ImageUrlCalculator.calculateBy(auctionImageAbsoluteUrl, auctionImage.getId())
        );
    }

    private static String calculateChatRoomRedirectUrl(final Long chatRoomId) {
        return "/chattings/" + chatRoomId;
    }

    private static String calculateAuctionRedirectUrl(final Long auctionId) {
        return "/auctions/" + auctionId;
    }
}
