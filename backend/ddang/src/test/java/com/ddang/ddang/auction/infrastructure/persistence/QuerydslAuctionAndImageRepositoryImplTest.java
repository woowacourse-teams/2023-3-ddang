package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.infrastructure.persistence.dto.AuctionAndImageDto;
import com.ddang.ddang.auction.infrastructure.persistence.fixture.QuerydslAuctionAndImageRepositoryImplFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuerydslAuctionAndImageRepositoryImplTest extends QuerydslAuctionAndImageRepositoryImplFixture {

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Test
    void 경매와_경매_대표이미지를_조회한다() {
        // when
        final Optional<AuctionAndImageDto> actual = auctionRepository.findDtoByAuctionId(경매.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().auction()).isEqualTo(경매);
            softAssertions.assertThat(actual.get().auctionImage()).isEqualTo(경매_이미지);
        });
    }
}
