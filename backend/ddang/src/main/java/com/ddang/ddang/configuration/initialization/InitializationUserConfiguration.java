package com.ddang.ddang.configuration.initialization;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@ConditionalOnProperty(name = "data.init.user.enabled", havingValue = "true")
@RequiredArgsConstructor
public class InitializationUserConfiguration implements ApplicationRunner {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        final User seller1 = User.builder()
                                 .name("판매자1")
                                 .profileImage(new ProfileImage("upload.png", "updateImage.png"))
                                 .reliability(new Reliability(4.7d))
                                 .oauthId("12345")
                                 .build();

        final User buyer1 = User.builder()
                                .name("구매자1")
                                .profileImage(new ProfileImage("upload.png", "updateImage.png"))
                                .reliability(new Reliability(3.0d))
                                .oauthId("12346")
                                .build();

        final User buyer2 = User.builder()
                                .name("구매자2")
                                .profileImage(new ProfileImage("upload.png", "updateImage.png"))
                                .reliability(new Reliability(0.8d))
                                .oauthId("12347")
                                .build();

        userRepository.save(seller1);
        userRepository.save(buyer1);
        userRepository.save(buyer2);
    }
}
