package com.ddang.ddang.notification.application;

import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.bid.application.event.BidNotificationEvent;
import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.event.MessageNotificationEvent;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.notification.application.fixture.NotificationEventListenerFixture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@IsolateDatabase
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NotificationEventListenerTest extends NotificationEventListenerFixture {

    @MockBean
    FirebaseMessaging firebaseMessaging;

    @Autowired
    NotificationEventListener notificationEventListener;

    @Autowired
    ApplicationEvents events;

    @Autowired
    MessageService messageService;

    @Autowired
    JpaMessageRepository messageRepository;

    @Autowired
    BidService bidService;

    @Test
    void 메시지를_전송하면_알림을_전송한다() {
        // when
        messageService.create(메시지_생성_DTO, 이미지_절대_경로);

        // then
        final long actual = events.stream(MessageNotificationEvent.class).count();
        assertThat(actual).isEqualTo(1);
    }

    @Test
    void 메시지_알림이_실패해도_메시지는_저장된다() throws FirebaseMessagingException {
        // when
        given(firebaseMessaging.send(any())).willThrow(FirebaseMessagingException.class);
        final Long actualSavedMessageId = messageService.create(메시지_생성_DTO, 이미지_절대_경로);
        final long actual = events.stream(MessageNotificationEvent.class).count();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(messageRepository.findById(actualSavedMessageId)).isPresent();
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
}
