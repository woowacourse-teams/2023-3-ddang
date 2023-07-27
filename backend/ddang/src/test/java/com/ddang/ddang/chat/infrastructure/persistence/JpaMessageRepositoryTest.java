package com.ddang.ddang.chat.infrastructure.persistence;

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
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        final User seller = new User("판매자", "이미지", 5.0);
        final User buyer = new User("구매자", "이미지", 5.0);
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        userRepository.save(seller);
        userRepository.save(buyer);
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, seller, buyer);
        chatRoomRepository.save(chatRoom);

        final Message message = new Message(chatRoom, seller, buyer, "안녕하세요.");

        // when
        messageRepository.save(message);

        // then
        em.flush();
        em.clear();

        assertThat(message.getId()).isPositive();
    }

    @Test
    void 지정한_아이디에_대한_메시지를_조회한다() {
        // given
        final User seller = new User("판매자", "이미지", 5.0);
        final User buyer = new User("구매자", "이미지", 5.0);
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        userRepository.save(seller);
        userRepository.save(buyer);
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, seller, buyer);

        chatRoomRepository.save(chatRoom);

        final Message expected = new Message(chatRoom, seller, buyer, "안녕하세요.");

        messageRepository.save(expected);

        em.flush();
        em.clear();

        // when
        final Optional<Message> actual = messageRepository.findById(expected.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().getId()).isEqualTo(expected.getId());
            softAssertions.assertThat(actual.get().getChatRoom()).isEqualTo(expected.getChatRoom());
            softAssertions.assertThat(actual.get().getWriter()).isEqualTo(expected.getWriter());
            softAssertions.assertThat(actual.get().getReceiver()).isEqualTo(expected.getReceiver());
            softAssertions.assertThat(actual.get().getContents()).isEqualTo(expected.getContents());
        });
    }
}
