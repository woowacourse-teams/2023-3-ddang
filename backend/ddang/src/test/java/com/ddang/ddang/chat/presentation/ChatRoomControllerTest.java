package com.ddang.ddang.chat.presentation;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.chat.application.ChatRoomService;
import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.dto.ReadAuctionDto;
import com.ddang.ddang.chat.application.dto.ReadMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadUserDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.MessageNotFoundException;
import com.ddang.ddang.chat.application.exception.UserNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.presentation.auth.UserIdArgumentResolver;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.chat.presentation.dto.request.ReadMessageRequest;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ChatRoomController.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatRoomControllerTest {

    @MockBean
    ChatRoomService chatRoomService;

    @MockBean
    MessageService messageService;

    @Autowired
    ChatRoomController chatRoomController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatRoomController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .setCustomArgumentResolvers(new UserIdArgumentResolver())
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void 메시지를_생성한다() throws Exception {
        // given
        final String contents = "메시지 내용";
        final CreateMessageRequest request = new CreateMessageRequest(1L, contents, LocalDateTime.now());

        given(messageService.create(any(CreateMessageDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/chattings/1/messages")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/chattings/1/messages/1")),
                       jsonPath("$.id", is(1L), Long.class)
               );
    }

    @Test
    void 채팅방이_없는_경우_메시지_생성시_404를_반환한다() throws Exception {
        // given
        final Long invalidChatRoomId = -999L;
        final String contents = "메시지 내용";
        final CreateMessageRequest request = new CreateMessageRequest(1L, contents, LocalDateTime.now());

        final ChatRoomNotFoundException chatRoomNotFoundException = new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");
        given(messageService.create(CreateMessageDto.of(1L, invalidChatRoomId, request)))
                .willThrow(chatRoomNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", invalidChatRoomId)
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(chatRoomNotFoundException.getMessage()))
               );
    }

    @Test
    void 발신자가_없는_경우_메시지_생성시_404를_반환한다() throws Exception {
        // given
        final Long invalidWriterId = -999L;
        final Long chatRoomId = 1L;
        final String contents = "메시지 내용";
        final CreateMessageRequest request = new CreateMessageRequest(1L, contents, LocalDateTime.now());

        final UserNotFoundException userNotFoundException = new UserNotFoundException("사용자 정보가 없습니다.");
        given(messageService.create(CreateMessageDto.of(invalidWriterId, chatRoomId, request)))
                .willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", chatRoomId)
                       .header(HttpHeaders.AUTHORIZATION, invalidWriterId)
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 사용자가_참여한_모든_채팅방을_조회한다() throws Exception {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        final User user1 = new User("상대1", "profileImage.png", 5.0);
        final User user2 = new User("상대2", "profileImage.png", 5.0);
        final Auction auction1 = Auction.builder()
                                        .title("경매 상품 1")
                                        .seller(user1)
                                        .subCategory(sub)
                                        .description("이것은 경매 상품 1 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
        auction1.addAuctionImages(List.of(new AuctionImage("사진", "image")));
        auction1.updateLastBid(new Bid(auction1, user2, new BidPrice(3000)));

        final Auction auction2 = Auction.builder()
                                        .title("경매 상품 2")
                                        .seller(user2)
                                        .subCategory(sub)
                                        .description("이것은 경매 상품 2 입니다.")
                                        .bidUnit(new BidUnit(2_000))
                                        .startPrice(new Price(2_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
        auction2.addAuctionImages(List.of(new AuctionImage("사진", "image")));
        auction2.updateLastBid(new Bid(auction2, user1, new BidPrice(5000)));

        final ReadParticipatingChatRoomDto chatRoom1 = new ReadParticipatingChatRoomDto(
                1L,
                ReadAuctionDto.from(auction1),
                ReadUserDto.from(user1),
                true
        );
        final ReadParticipatingChatRoomDto chatRoom2 = new ReadParticipatingChatRoomDto(
                2L,
                ReadAuctionDto.from(auction2),
                ReadUserDto.from(user2),
                true
        );

        given(chatRoomService.readAllByUserId(anyLong()))
                .willReturn(List.of(chatRoom1, chatRoom2));

        // when & then
        mockMvc.perform(get("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.chattings.[0].id", is(chatRoom1.id()), Long.class),
                       jsonPath("$.chattings.[0].chatPartner.name", is(user1.getName())),
                       jsonPath("$.chattings.[0].auction.title", is(auction1.getTitle())),
                       jsonPath("$.chattings.[1].id", is(chatRoom2.id()), Long.class),
                       jsonPath("$.chattings.[1].chatPartner.name", is(user2.getName())),
                       jsonPath("$.chattings.[1].auction.title", is(auction2.getTitle()))
               );
    }

    @Test
    void 사용자가_참여한_채팅방_목록_조회시_요청한_사용자_정보가_없다면_404를_반환한다() throws Exception {
        // given
        final Long invalidUserId = -999L;
        final UserNotFoundException userNotFoundException =
                new UserNotFoundException("사용자 정보를 찾을 수 없습니다.");
        given(chatRoomService.readAllByUserId(invalidUserId))
                .willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(get("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, invalidUserId)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 메시지를_조회한다() throws Exception {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        final User user1 = new User(1L, "상대1", "", 5.0);
        final User user2 = new User(2L, "상대2", "", 5.0);
        final Auction auction1 = Auction.builder()
                                        .title("경매 상품 1")
                                        .seller(user1)
                                        .subCategory(sub)
                                        .description("이것은 경매 상품 1 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
        auction1.addAuctionImages(List.of(new AuctionImage("사진", "image")));
        auction1.updateLastBid(new Bid(auction1, user2, new BidPrice(3000)));

        final ChatRoom chatRoom = new ChatRoom(auction1, user1);

        final String contents = "메시지 내용";
        final LocalDateTime createdAt = LocalDateTime.now();

        final ReadMessageDto readMessageDto = new ReadMessageDto(
                1L,
                createdAt,
                chatRoom,
                user1,
                user2,
                "메시지 내용"
        );
        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willReturn(List.of(readMessageDto));

        // when & then
        mockMvc.perform(get("/chattings/1/messages/1")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.[0].id", is(1L), Long.class),
                       jsonPath("$.[0].createdAt").exists(),
                       jsonPath("$.[0].isMyMessage", is(true), boolean.class),
                       jsonPath("$.[0].contents", is(contents), String.class)
               );
    }

    @Test
    void 잘못된_사용자가_메시지를_조회할_경우_404를_반환한다() throws Exception {
        // given
        final Long invalidUserId = -999L;
        final UserNotFoundException userNotFoundException =
                new UserNotFoundException("메시지 조회할 권한이 없는 사용자입니다.");
        new ReadMessageRequest(invalidUserId, 1L, 1L);
        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(get("/chattings/1/messages/1")
                       .header(HttpHeaders.AUTHORIZATION, invalidUserId)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 조회하고자_하는_채팅방이_없는_경우_404를_반환한다() throws Exception {
        // given
        final Long invalidLastMessageId = -999L;
        final MessageNotFoundException messageNotFoundException =
                new MessageNotFoundException("조회한 마지막 메시지가 존재하지 않습니다.");
        new ReadMessageRequest(1L, 1L, invalidLastMessageId);
        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willThrow(messageNotFoundException);

        // when & then
        mockMvc.perform(get("/chattings/1/messages/" + invalidLastMessageId)
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(messageNotFoundException.getMessage()))
               );
    }
}
