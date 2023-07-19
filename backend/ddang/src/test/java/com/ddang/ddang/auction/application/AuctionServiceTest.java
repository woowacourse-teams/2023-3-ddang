package com.ddang.ddang.auction.application;

import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionServiceTest {

    @Autowired
    AuctionService auctionService;

    @Test
    void 경매를_등록한다() {
        // given
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                // TODO 2차 데모데이 이후 리펙토링 예정
                "",
                "",
                "",
                "",
                "",
                ""
        );

        // when
        final Long actual = auctionService.create(createAuctionDto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 지정한_아이디에_해당하는_경매를_조회한다() {
        // given
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                // TODO 2차 데모데이 이후 리펙토링 예정
                "",
                "",
                "",
                "",
                "",
                ""
        );

        final Long savedAuctionId = auctionService.create(createAuctionDto);

        // when
        final ReadAuctionDto actual = auctionService.readByAuctionId(savedAuctionId);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.id()).isPositive();
            softAssertions.assertThat(actual.title()).isEqualTo(createAuctionDto.title());
            softAssertions.assertThat(actual.description()).isEqualTo(createAuctionDto.description());
            softAssertions.assertThat(actual.bidUnit()).isEqualTo(createAuctionDto.bidUnit());
            softAssertions.assertThat(actual.startBidPrice()).isEqualTo(createAuctionDto.startBidPrice());
            softAssertions.assertThat(actual.lastBidPrice()).isNull();
            softAssertions.assertThat(actual.winningBidPrice()).isNull();
            softAssertions.assertThat(actual.deleted()).isFalse();
            softAssertions.assertThat(actual.closingTime()).isEqualTo(createAuctionDto.closingTime());
        });
    }

    @Test
    void 지정한_아이디에_해당하는_경매를_삭제한다() {
        // given
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                // TODO 2차 데모데이 이후 리펙토링 예정
                "",
                "",
                "",
                "",
                "",
                ""
        );

        final Long savedAuctionId = auctionService.create(createAuctionDto);

        // when
        auctionService.deleteByAuctionId(savedAuctionId);

        // then
        final ReadAuctionDto actual = auctionService.readByAuctionId(savedAuctionId);
        assertThat(actual.deleted()).isTrue();
    }
}
