package com.ddang.ddang.auction.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.fixture.QuerydslAuctionRepositoryImplForObjectFixture;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class QuerydslAuctionRepositoryImplForObjectTest extends QuerydslAuctionRepositoryImplForObjectFixture {

    QuerydslAuctionRepository querydslAuctionRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory queryFactory) {
        querydslAuctionRepository = new QuerydslAuctionRepositoryImpl(queryFactory);
    }

    @Test
    void 지정한_아이디에_대한_경매를_조회한다() {
        // when
        final Optional<Auction> actual = querydslAuctionRepository.findAuctionById(경매.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();

            final Auction actualAuction = actual.get();
            softAssertions.assertThat(actualAuction.getTitle()).isEqualTo(경매.getTitle());
            softAssertions.assertThat(actualAuction.getId()).isEqualTo(경매.getId());
            softAssertions.assertThat(actualAuction.getAuctionRegions()).hasSize(1);

            final Region actualThirdRegion = actualAuction.getAuctionRegions().get(0).getThirdRegion();
            softAssertions.assertThat(actualThirdRegion.getName()).isEqualTo(개포1동.getName());

            final Region actualSecondRegion = actualThirdRegion.getSecondRegion();
            softAssertions.assertThat(actualSecondRegion.getName()).isEqualTo(강남구.getName());

            final Region actualFirstRegion = actualSecondRegion.getFirstRegion();
            softAssertions.assertThat(actualFirstRegion.getName()).isEqualTo(서울특별시.getName());

            final Category actualSubCategory = actual.get().getSubCategory();
            softAssertions.assertThat(actualSubCategory).isEqualTo(가구_서브_의자_카테고리);

            final Category mainCategory = actualSubCategory.getMainCategory();
            softAssertions.assertThat(mainCategory).isEqualTo(가구_카테고리);

            final User actualSeller = actual.get().getSeller();
            softAssertions.assertThat(actualSeller).isEqualTo(판매자);
        });
    }

    @Test
    void 지정한_아이디에_해당하는_경매가_없는_경우_빈_Optional을_조회한다() {
        // when
        final Optional<Auction> actual = querydslAuctionRepository.findAuctionById(존재하지_않는_경매);

        // then
        assertThat(actual).isEmpty();
    }
}
