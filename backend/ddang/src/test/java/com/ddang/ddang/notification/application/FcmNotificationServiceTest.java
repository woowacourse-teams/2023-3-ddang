package com.ddang.ddang.notification.application;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.configuration.fcm.exception.FcmNotFoundException;
import com.ddang.ddang.device.application.exception.DeviceTokenNotFoundException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FcmNotificationServiceTest extends FcmNotificationServiceFixture {

    @MockBean
    FirebaseMessaging firebaseMessaging;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Test
    void 알림을_전송한다() throws FirebaseMessagingException {
        // given
        given(firebaseMessaging.send(any(Message.class))).willReturn(알림_메시지_아이디);

        final NotificationStatus actual = notificationService.send(알림_생성_DTO);

        // then
        assertThat(actual).isEqualTo(NotificationStatus.SUCCESS);
    }

    @Test
    void 알림을_전송시_알림을_받을_사용자_기기_토큰을_찾을_수_없다면_예외를_던진다() throws FirebaseMessagingException {
        // when & then
        assertThatThrownBy(() -> notificationService.send(기기토큰이_없는_사용자의_알림_생성_DTO))
                .isInstanceOf(DeviceTokenNotFoundException.class)
                .hasMessageContaining("사용자의 기기 토큰을 찾을 수 없습니다.");
    }

    @Test
    void Fcm에_의한_알림_전송_실패시_예외를_던진다() throws FirebaseMessagingException {
        // given
        given(firebaseMessaging.send(any(Message.class))).willThrow(FirebaseMessagingException.class);

        // when & then
        assertThatThrownBy(() -> notificationService.send(알림_생성_DTO))
                .isInstanceOf(FirebaseMessagingException.class);
    }

    @Test
    void Fcm이_등록되지_않은_경우_알림_전송_실패시_예외를_던진다() throws FirebaseMessagingException {
        // given
        given(firebaseMessaging.send(any(Message.class))).willThrow(FcmNotFoundException.class);

        // when & then
        assertThatThrownBy(() -> notificationService.send(알림_생성_DTO))
                .isInstanceOf(FcmNotFoundException.class);
    }
}
