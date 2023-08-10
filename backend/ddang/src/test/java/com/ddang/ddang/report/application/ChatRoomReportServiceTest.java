package com.ddang.ddang.report.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.report.application.dto.CreateChatRoomReportDto;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

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
    void 쪽지방_신고를_등록한다() {
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

        userRepository.save(buyer);
        auctionRepository.save(auction);
        chatRoomRepository.save(chatRoom);

        final CreateChatRoomReportDto createChatRoomReportDto = new CreateChatRoomReportDto(
                buyer.getId(),
                "신고합니다.",
                chatRoom.getId()
        );

        // when
        final Long actual = chatRoomReportService.create(createChatRoomReportDto);

        // then
        assertThat(actual).isPositive();
    }
}
