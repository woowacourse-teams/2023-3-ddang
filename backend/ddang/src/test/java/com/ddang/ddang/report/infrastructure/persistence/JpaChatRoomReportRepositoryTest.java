package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class JpaChatRoomReportRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    JpaChatRoomReportRepository chatRoomReportRepository;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    void 채팅방_신고를_저장한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        final User buyer = User.builder()
                               .name("사용자")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12345")
                               .build();
        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        final ChatRoomReport chatRoomReport = new ChatRoomReport(buyer, chatRoom, "신고합니다.");

        auctionRepository.save(auction);
        userRepository.save(buyer);
        chatRoomRepository.save(chatRoom);

        // when
        chatRoomReportRepository.save(chatRoomReport);

        // then
        em.flush();
        em.clear();

        assertThat(chatRoomReport.getId()).isPositive();
    }

    @Test
    void 특정_채팅방_아이디와_신고자_아이디가_동일한_레코드가_존재한다면_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        final User buyer = User.builder()
                               .name("사용자")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12345")
                               .build();
        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        final ChatRoomReport chatRoomReport = new ChatRoomReport(buyer, chatRoom, "신고합니다.");

        auctionRepository.save(auction);
        userRepository.save(buyer);
        chatRoomRepository.save(chatRoom);
        chatRoomReportRepository.save(chatRoomReport);

        em.flush();
        em.clear();

        // when
        final boolean actual = chatRoomReportRepository.existsByChatRoomIdAndReporterId(chatRoom.getId(), buyer.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_채팅방_아이디와_신고자_아이디가_동일한_레코드가_존재하지_않는다면_거짓을_반환한다() {
        // given
        final long invalidChatRoom = -9999L;
        final long invalidUserId = -9999L;

        // when
        final boolean actual = chatRoomReportRepository.existsByChatRoomIdAndReporterId(invalidChatRoom, invalidUserId);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 전체_채팅방_신고_목록을_조회한다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage(new ProfileImage("upload.png", "store.png"))
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final Auction auction1 = Auction.builder()
                                        .seller(seller)
                                        .title("경매 상품 1")
                                        .description("이것은 경매 상품 1 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
        final Auction auction2 = Auction.builder()
                                        .seller(seller)
                                        .title("경매 상품 1")
                                        .description("이것은 경매 상품 1 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
        final Auction auction3 = Auction.builder()
                                        .seller(seller)
                                        .title("경매 상품 1")
                                        .description("이것은 경매 상품 1 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
        final User user1 = User.builder()
                               .name("사용자1")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final User user2 = User.builder()
                               .name("사용자2")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12347")
                               .build();
        final User user3 = User.builder()
                               .name("사용자3")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12348")
                               .build();
        final ChatRoom chatRoom1 = new ChatRoom(auction1, user1);
        final ChatRoom chatRoom2 = new ChatRoom(auction2, user2);
        final ChatRoom chatRoom3 = new ChatRoom(auction3, user3);
        final ChatRoomReport chatRoomReport1 = new ChatRoomReport(user1, chatRoom1, "신고합니다");
        final ChatRoomReport chatRoomReport2 = new ChatRoomReport(user2, chatRoom2, "신고합니다");
        final ChatRoomReport chatRoomReport3 = new ChatRoomReport(user3, chatRoom3, "신고합니다");

        userRepository.save(seller);
        auctionRepository.save(auction1);
        auctionRepository.save(auction2);
        auctionRepository.save(auction3);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        chatRoomRepository.save(chatRoom1);
        chatRoomRepository.save(chatRoom2);
        chatRoomRepository.save(chatRoom3);

        chatRoomReportRepository.save(chatRoomReport1);
        chatRoomReportRepository.save(chatRoomReport2);
        chatRoomReportRepository.save(chatRoomReport3);

        em.flush();
        em.clear();

        // when
        final List<ChatRoomReport> actual = chatRoomReportRepository.findAllByOrderByIdAsc();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get(0).getReporter()).isEqualTo(user1);
            softAssertions.assertThat(actual.get(0).getChatRoom()).isEqualTo(chatRoom1);
            softAssertions.assertThat(actual.get(0).getChatRoom().getAuction().getSeller()).isEqualTo(seller);
            softAssertions.assertThat(actual.get(1).getReporter()).isEqualTo(user2);
            softAssertions.assertThat(actual.get(1).getChatRoom()).isEqualTo(chatRoom2);
            softAssertions.assertThat(actual.get(1).getChatRoom().getAuction().getSeller()).isEqualTo(seller);
            softAssertions.assertThat(actual.get(2).getReporter()).isEqualTo(user3);
            softAssertions.assertThat(actual.get(2).getChatRoom()).isEqualTo(chatRoom3);
            softAssertions.assertThat(actual.get(2).getChatRoom().getAuction().getSeller()).isEqualTo(seller);
        });
    }
}
