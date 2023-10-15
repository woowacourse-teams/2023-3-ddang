package com.ddang.ddang.device.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.domain.repository.DeviceTokenRepository;
import com.ddang.ddang.device.infrastructure.persistence.fixture.DeviceTokenRepositoryImplFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
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
public class DeviceTokenRepositoryImplTest extends DeviceTokenRepositoryImplFixture {

    DeviceTokenRepository deviceTokenRepository;

    @BeforeEach
    void setUp(@Autowired final JpaDeviceTokenRepository jpaDeviceTokenRepository) {
        deviceTokenRepository = new DeviceTokenRepositoryImpl(jpaDeviceTokenRepository);
    }

    @Test
    void 기기토큰을_저장한다() {
        // when
        deviceTokenRepository.save(아이디가_없는_기기토큰);

        // then
        assertThat(아이디가_없는_기기토큰.getId()).isPositive();
    }

    @Test
    void 사용자_아이디에_해당하는_기기토큰을_조회한다() {
        // when
        final Optional<DeviceToken> actual = deviceTokenRepository.findByUserId(아이디가_있는_기기토큰_사용자.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get()).isEqualTo(아이디가_있는_기기토큰);
        });
    }

    @Test
    void 사용자_아이디에_해당하는_기기토큰을_삭제한다() {
        // when
        deviceTokenRepository.deleteByUserId(삭제할_기기토큰_사용자.getId());

        // then
        assertThat(deviceTokenRepository.findByUserId(삭제할_기기토큰_사용자.getId())).isEmpty();
    }
}
