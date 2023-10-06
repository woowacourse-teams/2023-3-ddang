package com.ddang.ddang.chat.domain;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class ChatRoomTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaBidRepository bidRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @ParameterizedTest
    @CsvSource(value = {"0:true", "9:true", "10:false"}, delimiter = ':')
    void 채팅방_비활성화_여부를_체크한다(final long plusDay, final boolean expected) {
        // given
        final User buyer = User.builder()
                               .name("회원")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12345")
                               .build();
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        userRepository.save(buyer);
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        em.flush();
        em.clear();

        // when
        final boolean actual = chatRoom.isChatAvailableTime(chatRoom.getCreatedTime().plusDays(plusDay));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주어진_사용자의_채팅상대를_반환한다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage(new ProfileImage("upload.png", "store.png"))
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("구매자")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Auction auction = Auction.builder()
                                       .title("title")
                                       .seller(seller)
                                       .build();

        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        em.flush();
        em.clear();

        // when
        final User actual = chatRoom.calculateChatPartnerOf(buyer);

        // then
        assertThat(actual).isEqualTo(seller);
    }

    @Test
    void 주어진_사용자가_판매자라면_채팅_참여자이다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage(new ProfileImage("upload.png", "store.png"))
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("구매자")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Auction auction = Auction.builder()
                                       .title("경매")
                                       .seller(seller)
                                       .build();
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        em.flush();
        em.clear();

        // when
        final boolean actual = chatRoom.isParticipant(seller);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 주어진_사용자가_구매자라면_채팅_참여자이다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage(new ProfileImage("upload.png", "store.png"))
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("구매자")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Auction auction = Auction.builder()
                                       .title("경매")
                                       .seller(seller)
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        auctionRepository.save(auction);

        final Bid bid = new Bid(auction, buyer, new BidPrice(15_000));
        bidRepository.save(bid);
        auction.updateLastBid(bid);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        em.flush();
        em.clear();

        // when
        final boolean actual = chatRoom.isParticipant(buyer);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 주어진_사용자가_판매자와_구매자_모두_아니라면_채팅_참여자가_아니다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage(new ProfileImage("upload.png", "store.png"))
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("구매자")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final User stranger = User.builder()
                                  .name("일반인")
                                  .profileImage(new ProfileImage("upload.png", "store.png"))
                                  .reliability(4.7d)
                                  .oauthId("12347")
                                  .build();
        userRepository.save(seller);
        userRepository.save(buyer);
        userRepository.save(stranger);

        final Auction auction = Auction.builder()
                                       .title("경매")
                                       .seller(seller)
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        auctionRepository.save(auction);

        final Bid bid = new Bid(auction, buyer, new BidPrice(15_000));
        bidRepository.save(bid);
        auction.updateLastBid(bid);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        em.flush();
        em.clear();

        // when
        final boolean actual = chatRoom.isParticipant(stranger);

        // then
        assertThat(actual).isFalse();
    }
}
