package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageDto;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class QuerydslChatRoomAndMessageRepositoryImplTest {

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
    QuerydslChatRoomAndMessageRepository querydslChatRoomAndMessageRepository;

    @Autowired
    JpaMessageRepository messageRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory queryFactory) {
        querydslChatRoomAndMessageRepository = new QuerydslChatRoomAndMessageRepositoryImpl(queryFactory);
    }

    @Test
    void 지정한_사용자_아이디가_포함된_채팅방을_조회한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User merry = User.builder()
                               .name("메리")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12345")
                               .build();
        final User encho = User.builder()
                               .name("엔초")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final User jamie = User.builder()
                               .name("제이미")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12347")
                               .build();
        final User zeeto = User.builder()
                               .name("지토")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
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

        final ChatRoom chatRoom1 = new ChatRoom(merryAuction, encho);
        final ChatRoom chatRoom2 = new ChatRoom(enchoAuction, zeeto);
        final ChatRoom chatRoom3 = new ChatRoom(jamieAuction, encho);
        chatRoomRepository.save(chatRoom1);
        chatRoomRepository.save(chatRoom2);
        chatRoomRepository.save(chatRoom3);

        final Message message1 = Message.builder()
                                        .chatRoom(chatRoom3)
                                        .contents("jamieEncho message 1")
                                        .writer(jamie)
                                        .receiver(encho)
                                        .build();
        messageRepository.save(message1);
        final Message message2 = Message.builder()
                                        .chatRoom(chatRoom2)
                                        .writer(encho)
                                        .receiver(zeeto)
                                        .contents("enchoZeeto message 1")
                                        .build();
        messageRepository.save(message2);
        final Message lastMessage1 = Message.builder()
                                            .chatRoom(chatRoom1)
                                            .writer(encho)
                                            .receiver(zeeto)
                                            .contents("merryZeeto message 1")
                                            .build();
        messageRepository.save(lastMessage1);
        final Message lastMessage2 = Message.builder()
                                            .chatRoom(chatRoom3)
                                            .writer(encho)
                                            .receiver(zeeto)
                                            .contents("jamieEncho message 2")
                                            .build();
        messageRepository.save(lastMessage2);
        final Message lastMessage3 = Message.builder()
                                            .chatRoom(chatRoom2)
                                            .writer(encho)
                                            .receiver(zeeto)
                                            .contents("enchoZeeto message 2")
                                            .build();
        messageRepository.save(lastMessage3);

        em.flush();
        em.clear();

        // when
        final List<ChatRoomAndMessageDto> actual =
                querydslChatRoomAndMessageRepository.findAllChatRoomInfoByUserIdOrderByLastMessage(encho.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0).chatRoom()).isEqualTo(chatRoom2);
            softAssertions.assertThat(actual.get(0).message()).isEqualTo(lastMessage3);
            softAssertions.assertThat(actual.get(1).chatRoom()).isEqualTo(chatRoom3);
            softAssertions.assertThat(actual.get(1).message()).isEqualTo(lastMessage2);
            softAssertions.assertThat(actual.get(2).chatRoom()).isEqualTo(chatRoom1);
            softAssertions.assertThat(actual.get(2).message()).isEqualTo(lastMessage1);
        });
    }
}
