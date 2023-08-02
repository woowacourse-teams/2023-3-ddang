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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class JpaChatRoomRepositoryTest {

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
    void 채팅방을_저장한다() {
        // given
        final User buyer = new User("구매자", "이미지", 5.0);
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        userRepository.save(buyer);
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);

        // when
        chatRoomRepository.save(chatRoom);

        // then
        em.flush();
        em.clear();

        assertThat(chatRoom.getId()).isPositive();
    }

    @Test
    void 지정한_아이디에_대한_채팅방을_조회한다() {
        // given
        final User buyer = new User("구매자", "이미지", 5.0);
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        userRepository.save(buyer);
        auctionRepository.save(auction);

        final ChatRoom expected = new ChatRoom(auction, buyer);

        chatRoomRepository.save(expected);

        em.flush();
        em.clear();

        // when
        final Optional<ChatRoom> actual = chatRoomRepository.findById(expected.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().getId()).isEqualTo(expected.getId());
            softAssertions.assertThat(actual.get().getAuction()).isEqualTo(expected.getAuction());
            softAssertions.assertThat(actual.get().getBuyer()).isEqualTo(expected.getBuyer());
        });
    }

    @Test
    void 지정한_사용자_아이디가_포함된_채팅방을_조회한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User merry = new User("메리", "이미지", 5.0);
        final User encho = new User("엔초", "이미지", 5.0);
        final User jamie = new User("제이미", "이미지", 5.0);
        final User zeeto = new User("지토", "이미지", 5.0);
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
        final List<ChatRoom> actual = chatRoomRepository.findAllParticipatingChatRoomByUserId(encho.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).getId()).isEqualTo(enchoZeeto.getId());
            softAssertions.assertThat(actual.get(1).getId()).isEqualTo(jamieEncho.getId());
        });
    }
}
