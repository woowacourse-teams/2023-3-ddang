package com.ddang.ddang.device.infrastructure.persistence.fixture;

import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.domain.repository.DeviceTokenRepository;
import com.ddang.ddang.device.infrastructure.persistence.DeviceTokenRepositoryImpl;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.ddang.ddang.user.infrastructure.persistence.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class DeviceTokenRepositoryImplFixture {

    private UserRepository userRepository;
    private DeviceTokenRepository deviceTokenRepository;

    protected DeviceToken 아이디가_없는_기기토큰;
    protected DeviceToken 아이디가_있는_기기토큰;
    protected DeviceToken 삭제할_기기토큰;
    protected User 아이디가_있는_기기토큰_사용자;
    protected User 삭제할_기기토큰_사용자;

    @BeforeEach
    void fixtureSetUp(
            @Autowired final JpaUserRepository jpaUserRepository,
            @Autowired final JpaDeviceTokenRepository jpaDeviceTokenRepository
    ) {
        userRepository = new UserRepositoryImpl(jpaUserRepository);
        deviceTokenRepository = new DeviceTokenRepositoryImpl(jpaDeviceTokenRepository);

        final User 아이디가_없는_기기토큰_사용자 = User.builder()
                                          .name("저장하지 않은 기기토큰 사용자")
                                          .profileImage(new ProfileImage("upload.png", "store.png"))
                                          .reliability(new Reliability(4.7d))
                                          .oauthId("98765")
                                          .build();
        아이디가_있는_기기토큰_사용자 = User.builder()
                               .name("기기토큰_사용자")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(new Reliability(4.7d))
                               .oauthId("12345")
                               .build();
        삭제할_기기토큰_사용자 = User.builder()
                           .name("삭제할 기기토큰_사용자")
                           .profileImage(new ProfileImage("upload.png", "store.png"))
                           .reliability(new Reliability(4.7d))
                           .oauthId("14392")
                           .build();
        userRepository.save(아이디가_없는_기기토큰_사용자);
        userRepository.save(아이디가_있는_기기토큰_사용자);
        userRepository.save(삭제할_기기토큰_사용자);

        아이디가_없는_기기토큰 = new DeviceToken(아이디가_없는_기기토큰_사용자, "아이디가 없는 기기토큰");
        아이디가_있는_기기토큰 = new DeviceToken(아이디가_있는_기기토큰_사용자, "아이디가_있는_기기토큰");
        삭제할_기기토큰 = new DeviceToken(삭제할_기기토큰_사용자, "삭제할_기기토큰");
        deviceTokenRepository.save(아이디가_있는_기기토큰);
        deviceTokenRepository.save(삭제할_기기토큰);
    }
}
