package com.ddang.ddang.notification.application;

import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.notification.application.fixture.FcmNotificationServiceFixture;
import com.ddang.ddang.notification.domain.NotificationStatus;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FcmNotificationServiceTest extends FcmNotificationServiceFixture {

    @MockBean
    FirebaseMessaging firebaseMessaging;

    @Autowired
    BidService bidService;

    @Autowired
    MessageService messageService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Test
    void 알림을_전송한다() throws FirebaseMessagingException {
        // given
        given(firebaseMessaging.send(any(Message.class))).willReturn(알림_메시지_아이디);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // when
                final NotificationStatus actual = notificationService.send(알림_생성_DTO);

                // then
                assertThat(actual).isEqualTo(NotificationStatus.SUCCESS);
            }
        });
    }

    @Test
    void 알림을_전송시_알림을_받을_사용자_기기_토큰을_찾을_수_없다면_실패를_반환한다() {
        // when
        final NotificationStatus actual = notificationService.send(기기토큰이_없는_사용자의_알림_생성_DTO);

        // then
        assertThat(actual).isEqualTo(NotificationStatus.FAIL);
    }

    @Test
    void Fcm에_의한_알림_전송_실패시_실패를_반환한다() throws FirebaseMessagingException {
        // given
        given(firebaseMessaging.send(any(Message.class))).willThrow(FirebaseMessagingException.class);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // when
                final NotificationStatus actual = notificationService.send(알림_생성_DTO);

                // then
                assertThat(actual).isEqualTo(NotificationStatus.FAIL);
            }
        });
    }

    @Test
    void 알림_전송용_메시지_DTO에_null이_포함된_경우_실패를_반환한다() {
        // when
        final NotificationStatus actual = notificationService.send(프로필_이미지가_null인_알림_생성_dto);

        // then
        assertThat(actual).isEqualTo(NotificationStatus.FAIL);
    }
}
