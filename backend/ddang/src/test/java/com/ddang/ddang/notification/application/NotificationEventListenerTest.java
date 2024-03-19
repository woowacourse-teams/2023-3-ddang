package com.ddang.ddang.notification.application;

import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.bid.application.event.BidNotificationEvent;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.chat.application.event.MessageNotificationEvent;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import com.ddang.ddang.chat.handler.ChatWebSocketHandleTextMessageProvider;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.notification.application.fixture.NotificationEventListenerFixture;
import com.ddang.ddang.notification.domain.NotificationStatus;
import com.ddang.ddang.websocket.handler.dto.SendMessageDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@IsolateDatabase
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NotificationEventListenerTest extends NotificationEventListenerFixture {

    @MockBean
    FirebaseMessaging firebaseMessaging;

    @MockBean
    NotificationService notificationService;

    @Autowired
    NotificationEventListener notificationEventListener;

    @Autowired
    ApplicationEvents events;

    @Autowired
    ChatWebSocketHandleTextMessageProvider chatWebSocketHandleTextMessageProvider;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    JpaBidRepository bidRepository;

    @Autowired
    BidService bidService;

    @Mock
    WebSocketSession session;

    @Test
    void 이벤트가_호출되면_메시지_알림을_전송한다() throws FirebaseMessagingException {
        // given
        given(notificationService.send(any())).willReturn(NotificationStatus.SUCCESS);

        // when
        notificationEventListener.sendMessageNotification(메시지_알림_이벤트);

        // then
        verify(notificationService).send(any());
    }

    @Test
    void 메시지를_전송하면_알림을_전송한다() throws Exception {
        // given
        given(session.getAttributes()).willReturn(세션_attribute_정보);

        // when
        chatWebSocketHandleTextMessageProvider.handleCreateSendMessage(session, 메시지_전송_데이터);

        // then
        final long actual = events.stream(MessageNotificationEvent.class).count();
        assertThat(actual).isEqualTo(1);
    }

    @Test
    void 메시지_알림_전송이_실패해도_메시지는_저장된다() throws Exception {
        // given
        given(session.getAttributes()).willReturn(세션_attribute_정보);
        given(firebaseMessaging.send(any())).willThrow(FirebaseMessagingException.class);

        // when
        final List<SendMessageDto> actualSendMessageDtos = chatWebSocketHandleTextMessageProvider.handleCreateSendMessage(
                session,
                메시지_전송_데이터
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualSendMessageDtos).hasSize(1);

            final long actual = events.stream(MessageNotificationEvent.class).count();
            softAssertions.assertThat(actual).isEqualTo(1);
        });
    }

    @Test
    void 상위_입찰자가_발생하면_이전_입찰자에게_알림을_전송한다() {
        // when
        bidService.create(입찰_생성_DTO, 이미지_절대_경로);

        // then
        final long actual = events.stream(BidNotificationEvent.class).count();
        assertThat(actual).isEqualTo(1);
    }

    @Test
    void 입찰_알림_전송이_실패해도_메시지는_저장된다() throws FirebaseMessagingException {
        // when
        given(firebaseMessaging.send(any())).willThrow(FirebaseMessagingException.class);
        final Long actualSavedMessageId = bidService.create(입찰_생성_DTO, 이미지_절대_경로);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(bidRepository.findById(actualSavedMessageId)).isPresent();

            final long actual = events.stream(BidNotificationEvent.class).count();
            softAssertions.assertThat(actual).isEqualTo(1);
        });
    }

    @Test
    void 이벤트가_호출되면_입찰_알림을_전송한다() throws FirebaseMessagingException {
        // given
        given(notificationService.send(any())).willReturn(NotificationStatus.SUCCESS);

        // when
        notificationEventListener.sendBidNotification(입찰_알림_이벤트);

        // then
        verify(notificationService).send(any());
    }

    @Test
    void 이벤트가_호출되면_질문_알림을_전송한다() throws FirebaseMessagingException {
        // given
        given(notificationService.send(any())).willReturn(NotificationStatus.SUCCESS);

        // when
        notificationEventListener.sendQuestionNotification(질문_알림_이벤트);

        // then
        verify(notificationService).send(any());
    }

    @Test
    void 이벤트가_호출되면_답변_알림을_전송한다() throws FirebaseMessagingException {
        // given
        given(notificationService.send(any())).willReturn(NotificationStatus.SUCCESS);

        // when
        notificationEventListener.sendAnswerNotification(답변_알림_이벤트);

        // then
        verify(notificationService).send(any());
    }
}
