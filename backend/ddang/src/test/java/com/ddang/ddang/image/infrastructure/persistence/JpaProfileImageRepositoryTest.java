package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.fixture.JpaProfileImageRepositoryFixture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
class JpaProfileImageRepositoryTest extends JpaProfileImageRepositoryFixture {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaProfileImageRepository profileImageRepository;

    @Test
    void 프로필_이미지를_저장한다() {
        // given
        final ProfileImage profileImage = new ProfileImage(업로드_이미지_파일명, 저장된_이미지_파일명);

        // when
        final ProfileImage actual = profileImageRepository.save(profileImage);

        em.flush();
        em.clear();

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 지정한_아이디에_해당하는_이미지를_조회한다() {
        // when
        final Optional<ProfileImage> actual = profileImageRepository.findById(프로필_이미지.getId());

        // then
        assertThat(actual).contains(프로필_이미지);
    }

    @Test
    void 지정한_아이디에_해당하는_이미지가_없는_경우_빈_Optional을_반환한다() {
        // when
        final Optional<ProfileImage> actual = profileImageRepository.findById(존재하지_않는_프로필_이미지_아이디);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 저장된_이름에_해당하는_이미지를_반환한다() {
        // when
        final Optional<ProfileImage> actual = profileImageRepository.findByStoreName(저장된_이미지_파일명);

        // then
        assertThat(actual).contains(프로필_이미지);
    }
}
