package com.ddang.ddang.notification.application;

import com.ddang.ddang.device.application.exception.DeviceTokenNotFoundException;
import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ddang.ddang.notification.application.util.NotificationProperty.BODY;
import static com.ddang.ddang.notification.application.util.NotificationProperty.IMAGE;
import static com.ddang.ddang.notification.application.util.NotificationProperty.NOTIFICATION_TYPE;
import static com.ddang.ddang.notification.application.util.NotificationProperty.REDIRECT_URL;
import static com.ddang.ddang.notification.application.util.NotificationProperty.TITLE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FcmNotificationService implements NotificationService {

    private static final String NOTIFICATION_SEND_SUCCESS = "알림 전송 성공";
    private static final String NOTIFICATION_SEND_FAIL = "알림 전송에 실패했습니다.";

    private final FirebaseMessaging firebaseMessaging;
    private final JpaDeviceTokenRepository deviceTokenRepository;

    @Override
    public String send(final CreateNotificationDto createNotificationDto) {
        final DeviceToken deviceToken = deviceTokenRepository.findByUserId(createNotificationDto.targetUserId())
                                                             .orElseThrow(() -> new DeviceTokenNotFoundException(
                                                                     "사용자의 기기 토큰을 찾을 수 없습니다."
                                                             ));

        return makeAndSendMessage(createNotificationDto, deviceToken);
    }

    private String makeAndSendMessage(
            final CreateNotificationDto createNotificationDto,
            final DeviceToken deviceToken
    ) {
        final AndroidConfig androidConfig = AndroidConfig.builder()
                                                         .setPriority(AndroidConfig.Priority.HIGH)
                                                         .build();
        final Message message = Message.builder()
                                       .setToken(deviceToken.getDeviceToken())
                                       .putData(NOTIFICATION_TYPE.getKeyName(), createNotificationDto.notificationType().getValue())
                                       .putData(IMAGE.getKeyName(), createNotificationDto.image())
                                       .putData(TITLE.getKeyName(), createNotificationDto.title())
                                       .putData(BODY.getKeyName(), createNotificationDto.body())
                                       .putData(REDIRECT_URL.getKeyName(), createNotificationDto.redirectUrl())
                                       .setAndroidConfig(androidConfig)
                                       .build();

        try {
            firebaseMessaging.send(message);
            return NOTIFICATION_SEND_SUCCESS;
        } catch (FirebaseMessagingException ex) {
            log.error("exception type : {}, ", ex.getClass().getSimpleName(), ex);
            return NOTIFICATION_SEND_FAIL;
        }
    }
}
