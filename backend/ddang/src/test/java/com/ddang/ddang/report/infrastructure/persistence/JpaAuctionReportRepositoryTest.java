package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.report.infrastructure.persistence.fixture.JpaAuctionReportRepositoryFixture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaAuctionReportRepositoryTest extends JpaAuctionReportRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    JpaAuctionReportRepository auctionReportRepository;

    @Test
    void 경매_신고를_저장한다() {
        // given
        final AuctionReport auctionReport = new AuctionReport(판매자, 경매, "신고합니다");

        // when
        auctionReportRepository.save(auctionReport);

        // then
        em.flush();
        em.clear();

        assertThat(auctionReport.getId()).isPositive();
    }

    @Test
    void 특정_경매_아이디와_신고자_아이디가_동일한_레코드가_존재하면_참을_반환한다() {
        // when
        final boolean actual = auctionReportRepository.existsByAuctionIdAndReporterId(경매.getId(), 신고자1.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_경매_아이디와_신고자_아이디가_동일한_레코드가_존재하지_않는다면_거짓을_반환한다() {
        // when
        final boolean actual = auctionReportRepository.existsByAuctionIdAndReporterId(
                존재하지_않는_경매_아이디,
                존재하지_않는_사용자_아이디
        );

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 전체_경매_신고_목록을_조회한다() {
        // when
        final List<AuctionReport> actual = auctionReportRepository.findAllByOrderByIdAsc();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).isEqualTo(경매_신고1);
            softAssertions.assertThat(actual.get(1)).isEqualTo(경매_신고2);
            softAssertions.assertThat(actual.get(2)).isEqualTo(경매_신고3);
        });
    }
}
