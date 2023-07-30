package com.ddang.ddang.chat.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MessageServiceTest {

    @Autowired
    MessageService messageService;

    @Autowired
    JpaMessageRepository messageRepository;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @Test
    void 메시지를_생성한다() {
        // given
        final Auction auction = createAuction();
        final User writer = createUser("발신자");
        final User receiver = createUser("수신자");
        final ChatRoom chatRoom = createChatRoom(auction, receiver);
        final String contents = "메시지 내용";

        final CreateMessageDto createMessageDto = new CreateMessageDto(
                chatRoom.getId(),
                writer.getId(),
                receiver.getId(),
                contents
        );

        // when
        final Long messageId = messageService.create(createMessageDto);

        // then
        assertThat(messageId).isPositive();
    }

    private ChatRoom createChatRoom(final Auction auction, final User buyer) {
        final ChatRoom chatRoom = new ChatRoom(auction, buyer);

        return chatRoomRepository.save(chatRoom);
    }

    private User createUser(final String userName) {
        final User user = new User(userName, "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg", 0.8);

        return userRepository.save(user);
    }

    private Auction createAuction() {
        final BidUnit bidUnit = new BidUnit(1_000);
        final Price startPrice = new Price(10_000);
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .description("description")
                                       .bidUnit(bidUnit)
                                       .startPrice(startPrice)
                                       .closingTime(LocalDateTime.now().plusDays(3L))
                                       .image("image")
                                       .mainCategory("mainCategory")
                                       .subCategory("subCategory")
                                       .build();

        return auctionRepository.save(auction);
    }
}
