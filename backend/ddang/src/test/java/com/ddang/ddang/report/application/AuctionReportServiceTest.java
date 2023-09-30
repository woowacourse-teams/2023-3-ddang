package com.ddang.ddang.report.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;
import com.ddang.ddang.report.application.exception.AlreadyReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReporterToAuctionException;
import com.ddang.ddang.report.application.fixture.AuctionReportServiceFixture;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionReportServiceTest extends AuctionReportServiceFixture {

    @Autowired
    AuctionReportService auctionReportService;

    @Test
    void 경매_신고를_등록한다() {
        // when
        final Long actual = auctionReportService.create(새로운_경매_신고_요청);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_사용자가_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionReportService.create(존재하지_않는_사용자의_경매_신고_요청))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_경매를_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionReportService.create(존재하지_않는_경매_신고_요청))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 본인이_등록한_경매를_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionReportService.create(판매자가_본인의_경매_신고_요청))
                .isInstanceOf(InvalidReporterToAuctionException.class)
                .hasMessage("본인 경매글입니다.");
    }

    @Test
    void 삭제한_경매를_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionReportService.create(삭제된_경매_신고_요청))
                .isInstanceOf(InvalidReportAuctionException.class)
                .hasMessage("이미 삭제된 경매입니다.");
    }

    @Test
    void 이미_신고한_경매를_동일_사용자가_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionReportService.create(이미_신고한_사용자가_경매_신고_요청))
                .isInstanceOf(AlreadyReportAuctionException.class)
                .hasMessage("이미 신고한 경매입니다.");
    }

    @Test
    void 전체_신고_목록을_조회한다() {
        // when
        final List<ReadAuctionReportDto> actual = auctionReportService.readAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get(0).reporterDto().id()).isEqualTo(이미_신고한_신고자1.getId());
            softAssertions.assertThat(actual.get(0).auctionDto().id()).isEqualTo(경매.getId());
            softAssertions.assertThat(actual.get(1).reporterDto().id()).isEqualTo(이미_신고한_신고자2.getId());
            softAssertions.assertThat(actual.get(1).auctionDto().id()).isEqualTo(경매.getId());
            softAssertions.assertThat(actual.get(2).reporterDto().id()).isEqualTo(이미_신고한_신고자3.getId());
            softAssertions.assertThat(actual.get(2).auctionDto().id()).isEqualTo(경매.getId());
        });
    }
}
