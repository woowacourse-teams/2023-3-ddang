package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.repository.ProfileImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.fixture.ProfileImageRepositoryImplFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

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
    void 프로필을_이미지_이름에_해당하는_프로필_이미지가_존재하면_참을_반환한다() {
        // when
        final boolean actual = profileImageRepository.existsByStoreName(존재하는_프로필_이미지_이름);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 프로필을_이미지_이름에_해당하는_프로필_이미지가_존재하지_않으면_참을_반환한다() {
        // when
        final boolean actual = profileImageRepository.existsByStoreName(존재하지_않는_프로필_이미지_이름);

        // then
        assertThat(actual).isFalse();
    }
}
