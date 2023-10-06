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
import com.ddang.ddang.image.domain.ProfileImage;
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

    @Autowired
    JpaMessageRepository messageRepository;

    @Test
    void 지정한_채팅방_아이디에_해당하는_채팅방을_조회한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원1")
                                .profileImage(new ProfileImage("upload.png", "store.png"))
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("회원2")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
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
                                .profileImage(new ProfileImage("upload.png", "store.png"))
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("회원2")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
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
