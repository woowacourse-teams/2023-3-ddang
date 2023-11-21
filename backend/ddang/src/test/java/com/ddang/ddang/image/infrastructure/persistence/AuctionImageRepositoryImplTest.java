package com.ddang.ddang.image.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.repository.AuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.fixture.AuctionImageRepositoryImplFixture;
import java.util.Optional;
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
        final Optional<AuctionImage> actual = auctionImageRepository.findByStoreName(
                경매_이미지.getImage().getStoreName()
        );

        // then
        assertThat(actual).contains(경매_이미지);
    }
}
