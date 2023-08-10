package com.ddang.ddang.report.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.report.application.dto.CreateChatRoomReportDto;
import com.ddang.ddang.report.application.exception.ChatRoomReportNotAccessibleException;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatRoomReportServiceTest {

    @Autowired
    ChatRoomReportService chatRoomReportService;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @Test
    void 채팅방_신고를_등록한다() {
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
        final User buyer = User.builder()
                               .name("구매자")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final ChatRoom chatRoom = new ChatRoom(auction, buyer);

        userRepository.save(seller);
        auctionRepository.save(auction);
        userRepository.save(buyer);
        chatRoomRepository.save(chatRoom);

        final CreateChatRoomReportDto createChatRoomReportDto = new CreateChatRoomReportDto(
                chatRoom.getId(),
                "신고합니다.",
                buyer.getId()
        );

        // when
        final Long actual = chatRoomReportService.create(createChatRoomReportDto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_사용자가_채팅방을_신고할시_예외가_발생한다() {
        // given
        final Long invalidUserId = -999L;

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
        final User buyer = User.builder()
                               .name("구매자")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final ChatRoom chatRoom = new ChatRoom(auction, buyer);

        userRepository.save(seller);
        auctionRepository.save(auction);
        userRepository.save(buyer);
        chatRoomRepository.save(chatRoom);

        final CreateChatRoomReportDto createChatRoomReportDto = new CreateChatRoomReportDto(
                chatRoom.getId(),
                "신고합니다.",
                invalidUserId
        );

        // when & then
        assertThatThrownBy(() -> chatRoomReportService.create(createChatRoomReportDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_채팅방을_신고할시_예외가_발생한다() {
        // given
        final Long invalidChatRoomId = -999L;

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
        final User buyer = User.builder()
                               .name("구매자")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();

        userRepository.save(seller);
        userRepository.save(buyer);
        auctionRepository.save(auction);

        final User invalidReporter = User.builder()
                                         .name("사용자")
                                         .profileImage("profile.png")
                                         .reliability(4.7d)
                                         .oauthId("12347")
                                         .build();
        userRepository.save(invalidReporter);

        final CreateChatRoomReportDto createChatRoomReportDto = new CreateChatRoomReportDto(
                invalidChatRoomId,
                "신고합니다.",
                invalidReporter.getId()
        );

        // when & then
        assertThatThrownBy(() -> chatRoomReportService.create(createChatRoomReportDto))
                .isInstanceOf(ChatRoomNotFoundException.class)
                .hasMessage("해당 채팅방을 찾을 수 없습니다.");
    }

    @Test
    void 판매자와_구매자_외의_사용자가_채팅방을_신고할시_예외가_발생한다() {
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
        final User buyer = User.builder()
                               .name("구매자")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final ChatRoom chatRoom = new ChatRoom(auction, buyer);

        userRepository.save(seller);
        auctionRepository.save(auction);
        userRepository.save(buyer);
        chatRoomRepository.save(chatRoom);

        final User invalidReporter = User.builder()
                                         .name("사용자")
                                         .profileImage("profile.png")
                                         .reliability(4.7d)
                                         .oauthId("12347")
                                         .build();
        userRepository.save(invalidReporter);

        final CreateChatRoomReportDto createChatRoomReportDto = new CreateChatRoomReportDto(
                chatRoom.getId(),
                "신고합니다.",
                invalidReporter.getId()
        );

        // when & then
        assertThatThrownBy(() -> chatRoomReportService.create(createChatRoomReportDto))
                .isInstanceOf(ChatRoomReportNotAccessibleException.class)
                .hasMessage("해당 채팅방을 신고할 권한이 없습니다.");
    }
}
