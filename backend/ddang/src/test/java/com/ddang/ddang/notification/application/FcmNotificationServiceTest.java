package com.ddang.ddang.notification.application;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationStatus;
import com.ddang.ddang.notification.domain.NotificationType;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FcmNotificationServiceTest {

    @MockBean
    FirebaseMessaging mockFirebaseMessaging;

    @Autowired
    NotificationService notificationService;

    @Autowired
    JpaDeviceTokenRepository userDeviceTokenRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    void 알림을_전송한다() throws FirebaseMessagingException {
        // given
        final User user = User.builder()
                              .name("회원")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        userRepository.save(user);
        final DeviceToken deviceToken = new DeviceToken(user, "deviceToken");
        userDeviceTokenRepository.save(deviceToken);
        final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                NotificationType.MESSAGE,
                user.getId(),
                "제목",
                "내용",
                "/redirectUrl",
                "image.png"
        );

        given(mockFirebaseMessaging.send(any(Message.class))).willReturn("returnMessage");

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // when
                final NotificationStatus actual = notificationService.send(createNotificationDto);

                // then
                assertThat(actual).isEqualTo(NotificationStatus.SUCCESS);
            }
        });
    }

    @Test
    void 알림을_전송시_알림을_받을_사용자_기기_토큰을_찾을_수_없다면_실패를_반환한다() {
        // given
        final Long invalidUserId = -999L;
        final CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                NotificationType.MESSAGE,
                invalidUserId,
                "제목",
                "내용",
                "/redirectUrl",
                "image.png"
        );

        // when
        final NotificationStatus actual = notificationService.send(createNotificationDto);

        // then
        assertThat(actual).isEqualTo(NotificationStatus.FAIL);
    }

    @Test
    void 알림을_전송을_실패한다() throws FirebaseMessagingException {
        // given
        final User user = User.builder()
                              .name("회원")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        userRepository.save(user);
        final DeviceToken deviceToken = new DeviceToken(user, "deviceToken");
        userDeviceTokenRepository.save(deviceToken);

        final CreateNotificationDto invalidDto = new CreateNotificationDto(
                NotificationType.MESSAGE,
                user.getId(),
                "제목",
                "내용",
                "/redirectUrl",
                "image.png"
        );

        given(mockFirebaseMessaging.send(any(Message.class))).willThrow(FirebaseMessagingException.class);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // when
                final NotificationStatus actual = notificationService.send(invalidDto);

                // then
                assertThat(actual).isEqualTo(NotificationStatus.FAIL);
            }
        });
    }
}
