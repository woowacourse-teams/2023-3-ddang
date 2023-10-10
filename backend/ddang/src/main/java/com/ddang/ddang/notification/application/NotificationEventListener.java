package com.ddang.ddang.notification.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.bid.application.dto.BidDto;
import com.ddang.ddang.bid.application.event.BidNotificationEvent;
import com.ddang.ddang.chat.application.dto.MessageDto;
import com.ddang.ddang.chat.application.event.MessageNotificationEvent;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationType;
import com.ddang.ddang.qna.application.dto.QuestionDto;
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
            final MessageDto messageDto = messageNotificationEvent.messageDto();
            final ProfileImage profileImage = messageDto.receiver().getProfileImage();
            final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                    NotificationType.MESSAGE,
                    messageDto.receiver().getId(),
                    messageDto.writer().getName(),
                    messageDto.contents(),
                    calculateRedirectUrl(MESSAGE_NOTIFICATION_REDIRECT_URI, messageDto.chatRoom().getId()),
                    ImageUrlCalculator.calculateBy(messageDto.profileImageAbsoluteUrl(), profileImage.getId())
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
            final Auction auction = bidDto.auctionAndImageDto().auction();
            final AuctionImage auctionImage = bidDto.auctionAndImageDto().auctionImage();
            final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                    NotificationType.BID,
                    bidDto.previousBidderId(),
                    auction.getTitle(),
                    BID_NOTIFICATION_MESSAGE_FORMAT,
                    calculateRedirectUrl(AUCTION_DETAIL_URI, auction.getId()),
                    ImageUrlCalculator.calculateBy(bidDto.auctionImageAbsoluteUrl(), auctionImage.getId())
            );
            notificationService.send(createNotificationDto);
        } catch (final FirebaseMessagingException ex) {
            log.error("exception type : {}, ", ex.getClass().getSimpleName(), ex);
        }
    }

    @TransactionalEventListener
    public void sendQuestionNotification(final QuestionNotificationEvent questionNotificationEvent) {
        try {
            final QuestionDto questionDto = questionNotificationEvent.questionDto();
            final Auction auction = questionDto.auction();
            final AuctionImage auctionImage = auction.getAuctionImages().get(FIRST_IMAGE_INDEX);
            final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                    NotificationType.QUESTION,
                    auction.getSeller().getId(),
                    auction.getTitle(),
                    questionDto.content(),
                    calculateRedirectUrl(AUCTION_DETAIL_URI, auction.getId()),
                    ImageUrlCalculator.calculateBy(questionDto.auctionImageAbsoluteUrl(), auctionImage.getId())
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
            final AuctionImage auctionImage = auction.getAuctionImages().get(FIRST_IMAGE_INDEX);
            final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                    NotificationType.ANSWER,
                    question.getWriter().getId(),
                    question.getContent(),
                    answer.getContent(),
                    calculateRedirectUrl(AUCTION_DETAIL_URI, auction.getId()),
                    ImageUrlCalculator.calculateBy(answerNotificationEvent.absoluteImageUrl(), auctionImage.getId())
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
