package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.repository.ProfileImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.fixture.ProfileImageRepositoryImplFixture;
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
@Import(QuerydslConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProfileImageRepositoryImplTest extends ProfileImageRepositoryImplFixture {

    ProfileImageRepository profileImageRepository;

    @BeforeEach
    void setUp(@Autowired final JpaProfileImageRepository jpaProfileImageRepository) {
        profileImageRepository = new ProfileImageRepositoryImpl(jpaProfileImageRepository);
    }

    @Test
    void 프로필을_이미지를_아이디를_통해_조회한다() {
        // when
        final Optional<ProfileImage> actual = profileImageRepository.findById(프로필_이미지.getId());

        // then
        assertThat(actual).contains(프로필_이미지);
    }

    @Test
    void 프로필을_이미지를_저장_이미지를_통해_조회한다() {
        // when
        final Optional<ProfileImage> actual = profileImageRepository.findByStoreName(프로필_이미지.getImage().getStoreName());

        // then
        assertThat(actual).contains(프로필_이미지);
    }
}
