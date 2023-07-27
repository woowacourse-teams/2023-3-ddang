package com.ddang.ddang.chat.domain;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatRoomTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @ParameterizedTest
    @CsvSource(value = {"0:true", "9:true", "10:false"}, delimiter = ':')
    void 채팅방_비활성화_여부를_체크한다(final long plusDay, final boolean expected) {
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

        em.flush();
        em.clear();

        // when
        final boolean actual = chatRoom.isChatAvailable(chatRoom.getCreatedTime().plusDays(plusDay));

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
