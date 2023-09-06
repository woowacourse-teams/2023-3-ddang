package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class QuerydslChatRoomRepositoryImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaCategoryRepository categoryRepository;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @Test
    void 지정한_사용자_아이디가_포함된_채팅방을_조회한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User merry = User.builder()
                               .name("메리")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12345")
                               .build();
        final User encho = User.builder()
                               .name("엔초")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final User jamie = User.builder()
                               .name("제이미")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12347")
                               .build();
        final User zeeto = User.builder()
                               .name("지토")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12348")
                               .build();

        userRepository.save(merry);
        userRepository.save(encho);
        userRepository.save(jamie);
        userRepository.save(zeeto);

        final Auction merryAuction = Auction.builder()
                                            .title("경매 1")
                                            .seller(merry)
                                            .subCategory(sub)
                                            .bidUnit(new BidUnit(1_000))
                                            .startPrice(new Price(10_000))
                                            .build();
        final Auction enchoAuction = Auction.builder()
                                            .title("경매 2")
                                            .seller(encho)
                                            .subCategory(sub)
                                            .bidUnit(new BidUnit(2_000))
                                            .startPrice(new Price(20_000))
                                            .build();
        final Auction jamieAuction = Auction.builder()
                                            .title("경매 3")
                                            .seller(jamie)
                                            .subCategory(sub)
                                            .bidUnit(new BidUnit(3_000))
                                            .startPrice(new Price(30_000))
                                            .build();

        auctionRepository.save(merryAuction);
        auctionRepository.save(enchoAuction);
        auctionRepository.save(jamieAuction);

        final ChatRoom merryZeeto = new ChatRoom(merryAuction, zeeto);
        final ChatRoom enchoZeeto = new ChatRoom(enchoAuction, zeeto);
        final ChatRoom jamieEncho = new ChatRoom(jamieAuction, encho);
        chatRoomRepository.save(merryZeeto);
        chatRoomRepository.save(enchoZeeto);
        chatRoomRepository.save(jamieEncho);

        em.flush();
        em.clear();

        // when
        final List<ChatRoom> actual = chatRoomRepository.findAllByUserId(encho.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(1).getId()).isEqualTo(enchoZeeto.getId());
            softAssertions.assertThat(actual.get(0).getId()).isEqualTo(jamieEncho.getId());
        });
    }

    @Test
    void 지정한_채팅방_아이디에_해당하는_채팅방을_조회한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("회원2")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Auction auction = Auction.builder()
                                       .title("경매 1")
                                       .seller(seller)
                                       .subCategory(sub)
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .build();
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        em.flush();
        em.clear();

        // when
        final Optional<ChatRoom> actual = chatRoomRepository.findChatRoomById(chatRoom.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get()).isEqualTo(chatRoom);
        });
    }

    @Test
    void 지정한_경매_아이디가_포함된_채팅방의_아이디를_조회한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("회원2")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Auction auction = Auction.builder()
                                       .title("경매 1")
                                       .seller(seller)
                                       .subCategory(sub)
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .build();
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        em.flush();
        em.clear();

        // when
        final Optional<Long> actual = chatRoomRepository.findChatRoomIdByAuctionId(auction.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get()).isEqualTo(chatRoom.getId());
        });
    }
}
