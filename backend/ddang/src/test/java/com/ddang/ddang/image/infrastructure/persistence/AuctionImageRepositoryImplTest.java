package com.ddang.ddang.image.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.repository.AuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.exception.AuctionImageNotFoundException;
import com.ddang.ddang.image.infrastructure.persistence.fixture.AuctionImageRepositoryImplFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionImageRepositoryImplTest extends AuctionImageRepositoryImplFixture {

    AuctionImageRepository auctionImageRepository;

    @BeforeEach
    void setUp(@Autowired final JpaAuctionImageRepository jpaAuctionImageRepository) {
        auctionImageRepository = new AuctionImageRepositoryImpl(jpaAuctionImageRepository);
    }

    @Test
    void 경매_이미지를_파일_이름을_통해_조회한다() {
        // when
        final AuctionImage actual = auctionImageRepository.getByStoreNameOrThrow(경매_이미지.getImage().getStoreName());

        // then
        assertThat(actual).isEqualTo(경매_이미지);
    }

    @Test
    void 지정한_파일_이름이_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionImageRepository.getByStoreNameOrThrow(존재하지_않는_경매_이미지_이름))
                .isInstanceOf(AuctionImageNotFoundException.class);
    }
}
