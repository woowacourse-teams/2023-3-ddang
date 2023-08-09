package com.ddang.ddang.report.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.application.exception.UserNotFoundException;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;
import com.ddang.ddang.report.application.exception.AlreadyReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReporterToAuctionException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionReportServiceTest {

    @Autowired
    AuctionReportService auctionReportService;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    void 경매_신고를_등록한다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final Auction auction = Auction.builder()
                                       .seller(seller)
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = User.builder()
                                .name("사용자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12346")
                                .build();

        userRepository.save(seller);
        auctionRepository.save(auction);
        userRepository.save(user);

        final LoginUserDto loginUserDto = new LoginUserDto(user.getId());
        final CreateAuctionReportDto createAuctionReportDto = new CreateAuctionReportDto(auction.getId(), "신고합니다");

        // when
        final Long actual = auctionReportService.create(loginUserDto, createAuctionReportDto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_사용자가_신고하는_경우_예외가_발생한다() {
        // given
        final Long invalidUserId = -9999L;
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final Auction auction = Auction.builder()
                                       .seller(seller)
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();

        userRepository.save(seller);
        auctionRepository.save(auction);

        final LoginUserDto loginUserDto = new LoginUserDto(invalidUserId);
        final CreateAuctionReportDto createAuctionReportDto = new CreateAuctionReportDto(auction.getId(), "신고합니다");

        // when & then
        assertThatThrownBy(() -> auctionReportService.create(loginUserDto, createAuctionReportDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_경매를_신고하는_경우_예외가_발생한다() {
        final Long invalidAuctionId = -9999L;
        final User user = User.builder()
                                .name("사용자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();

        userRepository.save(user);

        final LoginUserDto loginUserDto = new LoginUserDto(user.getId());
        final CreateAuctionReportDto createAuctionReportDto = new CreateAuctionReportDto(invalidAuctionId, "신고합니다");

        // when & then
        assertThatThrownBy(() -> auctionReportService.create(loginUserDto, createAuctionReportDto))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 본인이_등록한_경매를_신고하는_경우_예외가_발생한다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final Auction auction = Auction.builder()
                                       .seller(seller)
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();

        userRepository.save(seller);
        auctionRepository.save(auction);

        final LoginUserDto loginUserDto = new LoginUserDto(seller.getId());
        final CreateAuctionReportDto createAuctionReportDto = new CreateAuctionReportDto(auction.getId(), "신고합니다");

        // when & then
        assertThatThrownBy(() -> auctionReportService.create(loginUserDto, createAuctionReportDto))
                .isInstanceOf(InvalidReporterToAuctionException.class)
                .hasMessage("본인 경매글입니다.");
    }

    @Test
    void 삭제한_경매를_신고하는_경우_예외가_발생한다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final Auction auction = Auction.builder()
                                       .seller(seller)
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = User.builder()
                                .name("사용자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12346")
                                .build();

        userRepository.save(seller);
        auctionRepository.save(auction);
        userRepository.save(user);

        auction.delete();

        final LoginUserDto loginUserDto = new LoginUserDto(user.getId());
        final CreateAuctionReportDto createAuctionReportDto = new CreateAuctionReportDto(auction.getId(), "신고합니다");

        // when & then
        assertThatThrownBy(() -> auctionReportService.create(loginUserDto, createAuctionReportDto))
                .isInstanceOf(InvalidReportAuctionException.class)
                .hasMessage("이미 삭제된 경매입니다.");
    }

    @Test
    void 이미_신고한_경매를_신고하는_경우_예외가_발생한다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final Auction auction = Auction.builder()
                                       .seller(seller)
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = User.builder()
                                .name("사용자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12346")
                                .build();

        userRepository.save(seller);
        auctionRepository.save(auction);
        userRepository.save(user);

        final LoginUserDto loginUserDto = new LoginUserDto(user.getId());
        final CreateAuctionReportDto createAuctionReportDto = new CreateAuctionReportDto(auction.getId(), "신고합니다");
        auctionReportService.create(loginUserDto, createAuctionReportDto);

        // when & then
        assertThatThrownBy(() -> auctionReportService.create(loginUserDto, createAuctionReportDto))
                .isInstanceOf(AlreadyReportAuctionException.class)
                .hasMessage("이미 신고한 경매입니다.");
    }

    @Test
    void 전체_신고_목록을_조회한다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final Auction auction = Auction.builder()
                                       .seller(seller)
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user1 = User.builder()
                               .name("사용자1")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final User user2 = User.builder()
                               .name("사용자2")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12347")
                               .build();
        final User user3 = User.builder()
                               .name("사용자3")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12348")
                               .build();

        userRepository.save(seller);
        auctionRepository.save(auction);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        final CreateAuctionReportDto createAuctionReportDto = new CreateAuctionReportDto(auction.getId(), "신고합니다");

        final LoginUserDto loginUserDto1 = new LoginUserDto(user1.getId());
        auctionReportService.create(loginUserDto1, createAuctionReportDto);

        final LoginUserDto loginUserDto2 = new LoginUserDto(user2.getId());
        auctionReportService.create(loginUserDto2, createAuctionReportDto);

        final LoginUserDto loginUserDto3 = new LoginUserDto(user3.getId());
        auctionReportService.create(loginUserDto3, createAuctionReportDto);



        // when
        final List<ReadAuctionReportDto> actual = auctionReportService.readAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get(0).reporterDto().id()).isEqualTo(user1.getId());
            softAssertions.assertThat(actual.get(0).auctionDto().id()).isEqualTo(auction.getId());
            softAssertions.assertThat(actual.get(1).reporterDto().id()).isEqualTo(user2.getId());
            softAssertions.assertThat(actual.get(1).auctionDto().id()).isEqualTo(auction.getId());
            softAssertions.assertThat(actual.get(2).reporterDto().id()).isEqualTo(user3.getId());
            softAssertions.assertThat(actual.get(2).auctionDto().id()).isEqualTo(auction.getId());
        });
    }
}
