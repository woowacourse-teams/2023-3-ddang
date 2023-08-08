package com.ddang.ddang.chat.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class JpaMessageRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @Autowired
    JpaMessageRepository messageRepository;

    @Test
    void 메시지를_저장한다() {
        // given
        final User participant1 = User.builder()
                                      .name("판매자")
                                      .profileImage("profile.png")
                                      .reliability(4.7d)
                                      .oauthId("12345")
                                      .build();
        final User participant2 = User.builder()
                                      .name("구매자")
                                      .profileImage("profile.png")
                                      .reliability(4.7d)
                                      .oauthId("12346")
                                      .build();
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        userRepository.save(participant1);
        userRepository.save(participant2);
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, participant2);
        chatRoomRepository.save(chatRoom);

        final Message message = Message.builder()
                                       .chatRoom(chatRoom)
                                       .writer(participant1)
                                       .receiver(participant2)
                                       .contents("안녕하세요")
                                       .build();

        // when
        messageRepository.save(message);

        // then
        em.flush();
        em.clear();

        assertThat(message.getId()).isPositive();
    }
}
