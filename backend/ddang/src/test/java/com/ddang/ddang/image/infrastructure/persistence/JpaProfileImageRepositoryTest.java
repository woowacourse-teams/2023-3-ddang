package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.ProfileImage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class JpaProfileImageRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaProfileImageRepository imageRepository;

    @Test
    void 지정한_아이디에_해당하는_이미지를_조회한다() {
        // given
        final ProfileImage profileImage = new ProfileImage("uploadName", "storeName");

        imageRepository.save(profileImage);

        em.flush();
        em.clear();

        // when
        final Optional<ProfileImage> actual = imageRepository.findById(profileImage.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get()).isEqualTo(profileImage);
        });
    }

    @Test
    void 지정한_아이디에_해당하는_이미지가_없는_경우_빈_Optional을_반환한다() {
        // given
        final Long invalidImageId = -999L;

        // when
        final Optional<ProfileImage> actual = imageRepository.findById(invalidImageId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 저장된_이름에_해당하는_이미지를_반환한다() {
        // given
        final String storeName = "storeName.png";
        final ProfileImage expect = new ProfileImage("uploadName", storeName);

        imageRepository.save(expect);

        em.flush();
        em.clear();

        // when
        final Optional<ProfileImage> actual = imageRepository.findByStoreName(storeName);

        // then
        assertThat(actual).contains(expect);
    }
}
