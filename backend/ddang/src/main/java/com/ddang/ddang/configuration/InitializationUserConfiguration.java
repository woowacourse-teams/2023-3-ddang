package com.ddang.ddang.configuration;

import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class InitializationUserConfiguration implements ApplicationRunner {

    private final JpaUserRepository userRepository;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        final User seller1 = new User(
                "판매자1",
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                4.7
        );


        final User buyer1 = new User(
                "구매자1",
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                3.0
        );


        final User buyer2 = new User(
                "구매자2",
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                0.8
        );

        userRepository.save(seller1);
        userRepository.save(buyer1);
        userRepository.save(buyer2);
    }
}
