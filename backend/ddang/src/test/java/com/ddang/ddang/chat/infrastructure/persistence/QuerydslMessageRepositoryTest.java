package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAscillCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class QuerydslMessageRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaRegionRepository regionRepository;

    @Autowired
    JpaCategoryRepository categoryRepository;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @Autowired
    JpaMessageRepository messageRepository;

    QuerydslMessageRepository querydslMessageRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory queryFactory) {
        querydslMessageRepository = new QuerydslMessageRepositoryImpl(queryFactory);
    }

    @Test
    void 마지막으로_읽은_메시지_이후에_추가된_메시지를_조회한다() {
        // given
        final User seller = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("78923")
                                .build();
        final User buyer = User.builder()
                               .name("구매자")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12345")
                               .build();
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        userRepository.save(seller);
        userRepository.save(buyer);
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        final int messagesCount = 10;
        for (int count = 0; count < messagesCount; count++) {
            final Message message = Message.builder()
                                           .chatRoom(chatRoom)
                                           .writer(seller)
                                           .receiver(buyer)
                                           .contents("안녕하세요")
                                           .build();

            messageRepository.save(message);
        }

        em.flush();
        em.clear();

        // when
        final Long lastMessageId = 3L;
        final List<Message> messages = messageRepository.findMessagesAllByLastMessageId(
                seller.getId(),
                chatRoom.getId(),
                lastMessageId
        );

        // then
        assertThat(messages).hasSizeGreaterThanOrEqualTo(7);
    }

    @Test
    void 상대방이_메시지를_추가한_경우_마지막으로_읽은_메시지_이후의_메시지를_조회한다() {
        // given
        final User writer = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("78923")
                                .build();
        final User receiver = User.builder()
                                  .name("구매자")
                                  .profileImage("profile.png")
                                  .reliability(4.7d)
                                  .oauthId("12345")
                                  .build();

        userRepository.save(writer);
        userRepository.save(receiver);

        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, receiver);

        chatRoomRepository.save(chatRoom);

        final int messagesCount = 10;
        for (int count = 0; count < messagesCount; count++) {
            final Message message = Message.builder()
                                           .chatRoom(chatRoom)
                                           .writer(writer)
                                           .receiver(receiver)
                                           .contents("안녕하세요")
                                           .build();

            messageRepository.save(message);
        }

        em.flush();
        em.clear();

        // when
        final Long lastMessageId = 3L;
        final List<Message> messages = messageRepository.findMessagesAllByLastMessageId(
                receiver.getId(),
                chatRoom.getId(),
                lastMessageId
        );

        // then
        assertThat(messages).hasSizeGreaterThanOrEqualTo(7);
    }

    @Test
    void 조회하려는_메시지_아이디가_존재하지_않는_경우_거짓을_반환한다() {
        final Long invalidMessageId = -999L;

        assertThat(messageRepository.existsById(invalidMessageId)).isFalse();
    }
}
