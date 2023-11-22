package com.ddang.ddang.notification.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.bid.application.dto.BidDto;
import com.ddang.ddang.bid.application.event.BidNotificationEvent;
import com.ddang.ddang.chat.application.event.MessageNotificationEvent;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationType;
import com.ddang.ddang.qna.application.event.AnswerNotificationEvent;
import com.ddang.ddang.qna.application.event.QuestionNotificationEvent;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private static final String URI_DELIMITER = "/";
    private static final String MESSAGE_NOTIFICATION_REDIRECT_URI = "/chattings";
    private static final String AUCTION_DETAIL_URI = "/auctions";
    private static final String BID_NOTIFICATION_MESSAGE_FORMAT = "상위 입찰자가 나타났습니다. 구매를 원하신다면 더 높은 가격을 제시해 주세요.";
    private static final int FIRST_IMAGE_INDEX = 0;

    private final NotificationService notificationService;

    @TransactionalEventListener
    public void sendMessageNotification(final MessageNotificationEvent messageNotificationEvent) {
        try {
            final Message message = messageNotificationEvent.message();
            final ProfileImage profileImage = message.getWriter().getProfileImage();
            final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                    NotificationType.MESSAGE,
                    message.getReceiver().getId(),
                    message.getWriter().findName(),
                    message.getContents(),
                    calculateRedirectUrl(MESSAGE_NOTIFICATION_REDIRECT_URI, message.getChatRoom().getId()),
                    messageNotificationEvent.profileImageAbsoluteUrl() + profileImage.getStoreName()
            );
            notificationService.send(createNotificationDto);
        } catch (final FirebaseMessagingException ex) {
            log.error("exception type : {}, ", ex.getClass().getSimpleName(), ex);
        }
    }

    @TransactionalEventListener
    public void sendBidNotification(final BidNotificationEvent bidNotificationEvent) {
        try {
            final BidDto bidDto = bidNotificationEvent.bidDto();
            final Auction auction = bidDto.auction();
            final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                    NotificationType.BID,
                    bidDto.previousBidderId(),
                    auction.getTitle(),
                    BID_NOTIFICATION_MESSAGE_FORMAT,
                    calculateRedirectUrl(AUCTION_DETAIL_URI, auction.getId()),
                    bidDto.auctionImageAbsoluteUrl() + auction.getThumbnailImageStoreName()
            );
            notificationService.send(createNotificationDto);
        } catch (final FirebaseMessagingException ex) {
            log.error("exception type : {}, ", ex.getClass().getSimpleName(), ex);
        }
    }

    @TransactionalEventListener
    public void sendQuestionNotification(final QuestionNotificationEvent questionNotificationEvent) {
        try {
            final Question question = questionNotificationEvent.question();
            final Auction auction = question.getAuction();
            final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                    NotificationType.QUESTION,
                    auction.getSeller().getId(),
                    auction.getTitle(),
                    question.getContent(),
                    calculateRedirectUrl(AUCTION_DETAIL_URI, auction.getId()),
                    questionNotificationEvent.absoluteImageUrl() + auction.getThumbnailImageStoreName()
            );
            notificationService.send(createNotificationDto);
        } catch (final FirebaseMessagingException ex) {
            log.error("exception type : {}, ", ex.getClass().getSimpleName(), ex);
        }
    }

    @TransactionalEventListener
    public void sendAnswerNotification(final AnswerNotificationEvent answerNotificationEvent) {
        try {
            final Answer answer = answerNotificationEvent.answer();
            final Auction auction = answer.getQuestion().getAuction();
            final Question question = answer.getQuestion();
            final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                    NotificationType.ANSWER,
                    question.getWriter().getId(),
                    question.getContent(),
                    answer.getContent(),
                    calculateRedirectUrl(AUCTION_DETAIL_URI, auction.getId()),
                    answerNotificationEvent.absoluteImageUrl() + auction.getThumbnailImageStoreName()
            );
            notificationService.send(createNotificationDto);
        } catch (final FirebaseMessagingException ex) {
            log.error("exception type : {}, ", ex.getClass().getSimpleName(), ex);
        }
    }

    private String calculateRedirectUrl(final String uri, final Long id) {
        return uri + URI_DELIMITER + id;
    }
}
