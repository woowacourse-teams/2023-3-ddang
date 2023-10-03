package com.ddang.ddang.notification.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.bid.application.dto.BidNotificationDto;
import com.ddang.ddang.bid.application.event.BidNotificationEvent;
import com.ddang.ddang.chat.application.dto.MessageDto;
import com.ddang.ddang.chat.application.event.MessageNotificationEvent;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private static final String URI_DELIMITER = "/";
    private static final String MESSAGE_NOTIFICATION_REDIRECT_URI = "/chattings";
    private static final String BID_NOTIFICATION_REDIRECT_URI = "/auctions";
    private static final String BID_NOTIFICATION_MESSAGE_FORMAT = "상위 입찰자가 나타났습니다. 구매를 원하신다면 더 높은 가격을 제시해 주세요.";

    private final NotificationService notificationService;

    @EventListener
    public void sendMessageNotification(final MessageNotificationEvent messageNotificationEvent) {
        final MessageDto messageDto = messageNotificationEvent.messageDto();
        final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                NotificationType.MESSAGE,
                messageDto.receiver().getId(),
                messageDto.writer().getName(),
                messageDto.contents(),
                calculateRedirectUrl(MESSAGE_NOTIFICATION_REDIRECT_URI, messageDto.chatRoom().getId()),
                ImageUrlCalculator.calculateBy(messageDto.profileImageAbsoluteUrl(), messageDto.receiver()
                                                                                               .getProfileImage()
                                                                                               .getId())
        );
        notificationService.send(createNotificationDto);
    }

    private String calculateRedirectUrl(final String uri, final Long id) {
        return uri + URI_DELIMITER + id;
    }
}
