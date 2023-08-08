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

    // TODO: parameterizedTest 테스트별 환경 분리하는 방법 찾아보기
    @Test
    void 마지막으로_읽은_메시지_이후에_추가된_메시지를_조회한다() {
        // given
        final User participant1 = new User("판매자", "이미지", 5.0);
        final User participant2 = new User("구매자", "이미지", 5.0);
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        userRepository.save(participant1);
        userRepository.save(participant2);
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, participant2);
        chatRoomRepository.save(chatRoom);

        final int messagesCount = 10;
        for (int count = 0; count < messagesCount; count++) {
            final Message message = Message.builder()
                                           .chatRoom(chatRoom)
                                           .writer(participant1)
                                           .receiver(participant2)
                                           .contents("안녕하세요")
                                           .build();

            messageRepository.save(message);
        }

        em.flush();
        em.clear();

        // when
        final Long lastMessageId = 3L;
        final List<Message> messages = messageRepository.findMessagesAllByLastMessageId(
                participant1.getId(),
                chatRoom.getId(),
                lastMessageId
        );

        // then
        assertThat(messages).hasSize(7);
    }
}
