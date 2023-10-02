package com.ddang.ddang.device.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.fixture.JpaDeviceTokenRepositoryFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaDeviceTokenRepositoryTest extends JpaDeviceTokenRepositoryFixture {

    @Autowired
    JpaDeviceTokenRepository userDeviceTokenRepository;

    @Test
    void 주어진_사용자_아이디에_해당하는_기기토큰을_조회한다() {
        // when
        final Optional<DeviceToken> actual = userDeviceTokenRepository.findByUserId(사용자.getId());

        // then
        assertThat(actual).contains(사용자의_디바이스_토큰);
    }
}
