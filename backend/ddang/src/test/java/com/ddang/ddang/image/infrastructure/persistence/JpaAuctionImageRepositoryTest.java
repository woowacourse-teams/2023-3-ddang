package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.infrastructure.persistence.fixture.JpaAuctionImageRepositoryFixture;
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
class JpaAuctionImageRepositoryTest extends JpaAuctionImageRepositoryFixture {

    @Autowired
    JpaAuctionImageRepository auctionImageRepository;

    @Test
    void 경매_이미지를_저장한다() {
        // given
        final AuctionImage auctionImage = new AuctionImage(업로드_이미지_파일명, 저장된_이미지_파일명);

        // when
        final AuctionImage actual = auctionImageRepository.save(auctionImage);

        // then
        assertThat(actual.getId()).isPositive();
    }


    @Test
    void 경매_이미지_이름에_해당하는_경매_이미지가_존재하면_참을_반환한다() {
        // when
        final boolean actual = auctionImageRepository.existsByStoreName(존재하는_경매_이미지_이름);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 경매_이미지_이름에_해당하는_경매_이미지가_존재하지_않으면_거짓을_반환한다() {
        // when
        final boolean actual = auctionImageRepository.existsByStoreName(존재하지_않는_경매_이미지_이름);

        // then
        assertThat(actual).isFalse();
    }
}
