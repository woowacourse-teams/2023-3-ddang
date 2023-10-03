package com.ddang.ddang.notification.application.fixture;

import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.event.domain.SendNotificationEvent;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationType;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class FcmNotificationServiceFixture {

    @Autowired
    private JpaUserRepository userRepository;


    @Autowired
    JpaDeviceTokenRepository deviceTokenRepository;

    protected User 사용자;
    protected DeviceToken 기기토큰;
    protected SendNotificationEvent 알림_전송_이벤트_DTO;
    protected String 알림_메시지_아이디 = "notificationMessageId";
    protected SendNotificationEvent 기기토큰이_없는_사용자의_알림_전송_이벤트_DTO;

    private User 기기토큰이_없는_사용자;

    @BeforeEach
    void setUp() {
        사용자 = User.builder()
                  .name("사용자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(4.7d)
                  .oauthId("12345")
                  .build();
        기기토큰이_없는_사용자 = User.builder()
                           .name("기기토큰이 없는 사용자")
                           .profileImage(new ProfileImage("upload.png", "store.png"))
                           .reliability(4.7d)
                           .oauthId("12347")
                           .build();

        userRepository.save(사용자);
        userRepository.save(기기토큰이_없는_사용자);

        기기토큰 = new DeviceToken(사용자, "deviceToken");

        deviceTokenRepository.save(기기토큰);

        final CreateNotificationDto 알림_생성_DTO = new CreateNotificationDto(
                NotificationType.MESSAGE,
                사용자.getId(),
                "제목",
                "내용",
                "/redirectUrlForNotification",
                "image.png"
        );
        알림_전송_이벤트_DTO = new SendNotificationEvent(알림_생성_DTO);

        final CreateNotificationDto 기기토큰이_없는_사용자의_알림_생성_DTO = new CreateNotificationDto(
                NotificationType.MESSAGE,
                기기토큰이_없는_사용자.getId(),
                "제목",
                "내용",
                "/redirectUrl",
                "image.png"
        );
        기기토큰이_없는_사용자의_알림_전송_이벤트_DTO = new SendNotificationEvent(기기토큰이_없는_사용자의_알림_생성_DTO);
    }
}
