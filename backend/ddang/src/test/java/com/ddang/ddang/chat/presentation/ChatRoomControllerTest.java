package com.ddang.ddang.chat.presentation;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.chat.application.ChatRoomService;
import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadUserDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.UserNotFoundException;
import com.ddang.ddang.chat.presentation.auth.UserIdArgumentResolver;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
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
        final CreateMessageRequest request = new CreateMessageRequest(
                1L,
                1L,
                contents
        );

        given(messageService.create(any(CreateMessageDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/chattings/1/messages")
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
        final CreateMessageRequest request = new CreateMessageRequest(1L, 1L, contents);

        final ChatRoomNotFoundException chatRoomNotFoundException =
                new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");
        given(messageService.create(CreateMessageDto.from(invalidChatRoomId, request)))
                .willThrow(chatRoomNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", invalidChatRoomId)
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
        final CreateMessageRequest request = new CreateMessageRequest(invalidWriterId, 1L, contents);

        final UserNotFoundException userNotFoundException =
                new UserNotFoundException("지정한 아이디에 대한 발신자를 찾을 수 없습니다.");
        given(messageService.create(CreateMessageDto.from(chatRoomId, request)))
                .willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", chatRoomId)
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
        final User user1 = new User("상대1", "", 5.0);
        final User user2 = new User("상대2", "", 5.0);
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
        auction1.updateLastBidPrice(new Bid(auction1, user2, new Price(3000)));

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
        auction2.updateLastBidPrice(new Bid(auction2, user1, new Price(5000)));

        final ReadParticipatingChatRoomDto chatRoom1 = new ReadParticipatingChatRoomDto(
                1L,
                ReadAuctionDto.from(auction1),
                ReadUserDto.from(user1)
        );
        final ReadParticipatingChatRoomDto chatRoom2 = new ReadParticipatingChatRoomDto(
                2L,
                ReadAuctionDto.from(auction2),
                ReadUserDto.from(user2)
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
    void 요청한_사용자_정보가_없다면_404를_반환한다() throws Exception {
        // given
        final Long invalidUserId = -999L;
        final UserNotFoundException userNotFoundException =
                new UserNotFoundException("지정한 아이디에 대한 발신자를 찾을 수 없습니다.");
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
}
