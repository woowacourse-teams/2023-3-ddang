package com.ddang.ddang.bid.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.CreateUserDto;
import com.ddang.ddang.bid.application.exception.InvalidAuctionToBidException;
import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import com.ddang.ddang.bid.application.exception.InvalidBidderException;
import com.ddang.ddang.bid.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidServiceTest {

    @Autowired
    BidService bidService;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    void 입찰을_등록한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = new User("사용자", "이미지", 4.9);

        auctionRepository.save(auction);
        userRepository.save(user);

        final CreateUserDto createUserDto = new CreateUserDto(user.getId());
        final CreateBidDto createBidDto = new CreateBidDto(auction.getId(), 10_000);

        // when
        final Long actual = bidService.create(createUserDto, createBidDto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 마지막_입찰자와_다른_사람은_마지막_입찰액과_최소_입찰단위를_더한_금액_이상의_금액으로_입찰을_등록할_수_있다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user1 = new User("사용자1", "이미지1", 4.9);
        final User user2 = new User("사용자2", "이미지2", 3.4);

        auctionRepository.save(auction);
        userRepository.save(user1);
        userRepository.save(user2);

        final CreateUserDto createUserDto1 = new CreateUserDto(user1.getId());
        final CreateUserDto createUserDto2 = new CreateUserDto(user2.getId());
        final CreateBidDto createBidDto1 = new CreateBidDto(auction.getId(), 10_000);
        final CreateBidDto createBidDto2 = new CreateBidDto(auction.getId(), 14_000);

        bidService.create(createUserDto1, createBidDto1);

        // when
        final Long actual = bidService.create(createUserDto2, createBidDto2);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 첫_입찰자는_시작가를_입찰로_등록할_수_있다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);

        auctionRepository.save(auction);
        userRepository.save(user);

        final CreateUserDto createUserDto = new CreateUserDto(user.getId());
        final CreateBidDto createBidDto = new CreateBidDto(auction.getId(), 1_000);

        // when
        final Long actual = bidService.create(createUserDto, createBidDto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_경매에_입찰하는_경우_예외가_발생한다() {
        // given
        final User user = new User("사용자1", "이미지1", 4.9);
        userRepository.save(user);

        final CreateUserDto createUserDto = new CreateUserDto(user.getId());
        final CreateBidDto createBidDto = new CreateBidDto(9999L, 10_000);

        // when & then
        assertThatThrownBy(() -> bidService.create(createUserDto, createBidDto))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_사용자가_경매에_입찰하는_경우_예외가_발생한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();

        auctionRepository.save(auction);

        final CreateUserDto createUserDto = new CreateUserDto(9999L);
        final CreateBidDto createBidDto = new CreateBidDto(auction.getId(), 10_000);

        // when & then
        assertThatThrownBy(() -> bidService.create(createUserDto, createBidDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 첫_입찰자가_시작가_낮은_금액으로_입찰하는_경우_예외가_발생한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);

        auctionRepository.save(auction);
        userRepository.save(user);

        final CreateUserDto createUserDto = new CreateUserDto(user.getId());
        final CreateBidDto createBidDto = new CreateBidDto(auction.getId(), 900);

        // when && then
        assertThatThrownBy(() -> bidService.create(createUserDto, createBidDto))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("입찰 금액이 잘못되었습니다");
    }

    @Test
    void 종료된_경매에_입찰하는_경우_예외가_발생한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().minusDays(1))
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);

        auctionRepository.save(auction);
        userRepository.save(user);

        final CreateUserDto createUserDto = new CreateUserDto(user.getId());
        final CreateBidDto createBidDto = new CreateBidDto(auction.getId(), 10_000);

        // when & then
        assertThatThrownBy(() -> bidService.create(createUserDto, createBidDto))
                .isInstanceOf(InvalidAuctionToBidException.class)
                .hasMessage("이미 종료된 경매입니다");
    }

    @Test
    void 삭제된_경매에_입찰하는_경우_예외가_발생한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);

        auctionRepository.save(auction);
        userRepository.save(user);

        final CreateUserDto createUserDto = new CreateUserDto(user.getId());
        final CreateBidDto createBidDto = new CreateBidDto(auction.getId(), 10_000);
        auction.delete();

        // when & then
        assertThatThrownBy(() -> bidService.create(createUserDto, createBidDto))
                .isInstanceOf(InvalidAuctionToBidException.class)
                .hasMessage("삭제된 경매입니다");
    }

    @Test
    void 마지막_입찰자가_연속으로_입찰하는_경우_예외가_발생한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);

        auctionRepository.save(auction);
        userRepository.save(user);

        final CreateUserDto createUserDto1 = new CreateUserDto(user.getId());
        final CreateBidDto createBidDto1 = new CreateBidDto(auction.getId(), 10_000);
        bidService.create(createUserDto1, createBidDto1);

        final CreateUserDto createUserDto2 = new CreateUserDto(user.getId());
        final CreateBidDto createBidDto2 = new CreateBidDto(auction.getId(), 12_000);

        // when && then
        assertThatThrownBy(() -> bidService.create(createUserDto2, createBidDto2))
                .isInstanceOf(InvalidBidderException.class)
                .hasMessage("이미 최고 입찰자입니다");
    }

    @Test
    void 마지막_입찰액보다_낮은_금액으로_입찰하는_경우_예외가_발생한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user1 = new User("사용자1", "이미지1", 4.9);
        final User user2 = new User("사용자2", "이미지2", 4.9);

        auctionRepository.save(auction);
        userRepository.save(user1);
        userRepository.save(user2);

        final CreateUserDto createUserDto1 = new CreateUserDto(user1.getId());
        final CreateBidDto createBidDto1 = new CreateBidDto(auction.getId(), 10_000);
        bidService.create(createUserDto1, createBidDto1);

        final CreateUserDto createUserDto2 = new CreateUserDto(user2.getId());
        final CreateBidDto createBidDto2 = new CreateBidDto(auction.getId(), 8_000);

        // when & then
        assertThatThrownBy(() -> bidService.create(createUserDto2, createBidDto2))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("마지막 입찰 금액보다 낮은 금액을 입력했습니다");
    }

    @Test
    void 마지막_입찰자와_다른_사람은_마지막_입찰액과_최소_입찰단위를_더한_금액보다_낮은_금액으로_입찰하는_경우_예외가_발생한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user1 = new User("사용자1", "이미지1", 4.9);
        final User user2 = new User("사용자2", "이미지2", 3.4);

        auctionRepository.save(auction);
        userRepository.save(user1);
        userRepository.save(user2);

        final CreateUserDto createUserDto1 = new CreateUserDto(user1.getId());
        final CreateUserDto createUserDto2 = new CreateUserDto(user2.getId());
        final CreateBidDto createBidDto1 = new CreateBidDto(auction.getId(), 10_000);
        final CreateBidDto createBidDto2 = new CreateBidDto(auction.getId(), 10_800);

        bidService.create(createUserDto1, createBidDto1);

        // when && then
        assertThatThrownBy(() -> bidService.create(createUserDto2, createBidDto2))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("입찰 금액이 잘못되었습니다");
    }

    @Test
    void 최소_입찰_단위보다_낮은_금액으로_입찰하는_경우_예외가_발생한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user1 = new User("사용자1", "이미지1", 4.9);
        final User user2 = new User("사용자2", "이미지2", 4.9);

        auctionRepository.save(auction);
        userRepository.save(user1);
        userRepository.save(user2);

        final CreateUserDto createUserDto1 = new CreateUserDto(user1.getId());
        final CreateBidDto createBidDto1 = new CreateBidDto(auction.getId(), 10_000);
        bidService.create(createUserDto1, createBidDto1);

        final CreateUserDto createUserDto2 = new CreateUserDto(user2.getId());
        final CreateBidDto createBidDto2 = new CreateBidDto(auction.getId(), 10_500);

        // when & then
        assertThatThrownBy(() -> bidService.create(createUserDto2, createBidDto2))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("입찰 금액이 잘못되었습니다");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 2_100_000_001})
    void 범위_밖의_금액으로_입찰하는_경우_예외가_발생한다(final int bidPrice) {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);

        auctionRepository.save(auction);
        userRepository.save(user);

        final CreateUserDto createUserDto = new CreateUserDto(user.getId());
        final CreateBidDto createBidDto = new CreateBidDto(auction.getId(), bidPrice);

        // when & then
        assertThatThrownBy(() -> bidService.create(createUserDto, createBidDto))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("입찰 금액이 잘못되었습니다");
    }
}
