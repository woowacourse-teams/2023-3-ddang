package com.ddang.ddang.chat.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.UserNotFoundException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Autowired
    JpaCategoryRepository categoryRepository;

    @Test
    void 메시지를_생성한다() {
        // given
        final BidUnit bidUnit = new BidUnit(1_000);
        final Price startPrice = new Price(10_000);
        final Category main = new Category("전자기기");
        final Category sub = new Category("노트북");

        main.addSubCategory(sub);

        categoryRepository.save(main);
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .description("description")
                                       .bidUnit(bidUnit)
                                       .startPrice(startPrice)
                                       .closingTime(LocalDateTime.now().plusDays(3L))
                                       .subCategory(sub)
                                       .build();

        auctionRepository.save(auction);

        final User writer = new User(
                "발신자",
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                0.8
        );

        userRepository.save(writer);

        final User receiver = new User(
                "수신자",
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                0.8
        );

        userRepository.save(receiver);

        final ChatRoom chatRoom = new ChatRoom(auction, writer);

        chatRoomRepository.save(chatRoom);

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

    @Test
    void 채팅방이_없는_경우_메시지를_생성하면_예외가_발생한다() {
        // given
        final BidUnit bidUnit = new BidUnit(1_000);
        final Price startPrice = new Price(10_000);
        final Category main = new Category("전자기기");
        final Category sub = new Category("노트북");

        main.addSubCategory(sub);

        categoryRepository.save(main);
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .description("description")
                                       .bidUnit(bidUnit)
                                       .startPrice(startPrice)
                                       .closingTime(LocalDateTime.now().plusDays(3L))
                                       .subCategory(sub)
                                       .build();

        auctionRepository.save(auction);

        final User writer = new User(
                "발신자",
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                0.8
        );

        userRepository.save(writer);

        final User receiver = new User(
                "수신자",
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                0.8
        );

        userRepository.save(receiver);

        final Long invalidChatRoomId = -999L;
        final String contents = "메시지 내용";

        final CreateMessageDto createMessageDto = new CreateMessageDto(
                invalidChatRoomId,
                writer.getId(),
                receiver.getId(),
                contents
        );

        // when & then
        assertThatThrownBy(() -> messageService.create(createMessageDto))
                .isInstanceOf(ChatRoomNotFoundException.class)
                .hasMessageContaining("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");
    }

    @Test
    void 발신자가_없는_경우_메시지를_생성하면_예외가_발생한다() {
        // given
        final BidUnit bidUnit = new BidUnit(1_000);
        final Price startPrice = new Price(10_000);
        final Category main = new Category("전자기기");
        final Category sub = new Category("노트북");

        main.addSubCategory(sub);

        categoryRepository.save(main);

        final Auction auction = Auction.builder()
                                       .title("title")
                                       .description("description")
                                       .bidUnit(bidUnit)
                                       .startPrice(startPrice)
                                       .closingTime(LocalDateTime.now().plusDays(3L))
                                       .subCategory(sub)
                                       .build();

        auctionRepository.save(auction);

        final User receiver = new User(
                "수신자",
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                0.8
        );

        userRepository.save(receiver);


        final ChatRoom chatRoom = new ChatRoom(auction, receiver);

        chatRoomRepository.save(chatRoom);

        final String contents = "메시지 내용";
        final Long invalidWriterId = -999L;

        final CreateMessageDto createMessageDto = new CreateMessageDto(
                chatRoom.getId(),
                invalidWriterId,
                receiver.getId(),
                contents
        );

        assertThatThrownBy(() -> messageService.create(createMessageDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("지정한 아이디에 대한 발신자를 찾을 수 없습니다.");
    }

    @Test
    void 수신자가_없는_경우_메시지를_생성하면_예외가_발생한다() {
        // given
        final BidUnit bidUnit = new BidUnit(1_000);
        final Price startPrice = new Price(10_000);
        final Category main = new Category("전자기기");
        final Category sub = new Category("노트북");

        main.addSubCategory(sub);

        categoryRepository.save(main);

        final Auction auction = Auction.builder()
                                       .title("title")
                                       .description("description")
                                       .bidUnit(bidUnit)
                                       .startPrice(startPrice)
                                       .closingTime(LocalDateTime.now().plusDays(3L))
                                       .subCategory(sub)
                                       .build();

        auctionRepository.save(auction);

        final User writer = new User(
                "발신자",
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                0.8
        );

        userRepository.save(writer);


        final ChatRoom chatRoom = new ChatRoom(auction, writer);

        chatRoomRepository.save(chatRoom);

        final Long invalidReceiverId = -999L;
        final String contents = "메시지 내용";

        final CreateMessageDto createMessageDto = new CreateMessageDto(
                chatRoom.getId(),
                writer.getId(),
                invalidReceiverId,
                contents
        );

        assertThatThrownBy(() -> messageService.create(createMessageDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("지정한 아이디에 대한 수신자를 찾을 수 없습니다.");
    }
}
