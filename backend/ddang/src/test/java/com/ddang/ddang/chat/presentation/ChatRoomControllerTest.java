package com.ddang.ddang.chat.presentation;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.chat.application.ChatRoomService;
import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.dto.ReadAuctionInChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadUserInChatRoomDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.InvalidAuctionToChatException;
import com.ddang.ddang.chat.application.exception.UserNotAccessibleException;
import com.ddang.ddang.chat.presentation.auth.UserIdArgumentResolver;
import com.ddang.ddang.chat.presentation.dto.request.CreateChatRoomRequest;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

@WebMvcTest(controllers = {ChatRoomController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.ddang\\.ddang\\.authentication\\.configuration\\..*")
        }
)
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
        given(messageService.create(CreateMessageDto.of(invalidChatRoomId, request)))
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
        given(messageService.create(CreateMessageDto.of(chatRoomId, request)))
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
        ReadUserInChatRoomDto seller = new ReadUserInChatRoomDto(1L, "사용자1", "profile.png", 5.0d);
        final ReadUserInChatRoomDto buyer1 = new ReadUserInChatRoomDto(2L, "사용자2", "profile.png", 5.0d);
        final ReadUserInChatRoomDto buyer2 = new ReadUserInChatRoomDto(3L, "사용자3", "profile.png", 5.0d);
        final ReadChatRoomWithLastMessageDto dto1 = new ReadChatRoomWithLastMessageDto(
                1L,
                new ReadAuctionInChatRoomDto(1L, "경매1", 10_000, List.of(1L, 2L), "main", "sub", seller.id(), seller.profileImage(), seller.name(), seller.reliability()),
                buyer1,
                new ReadLastMessageDto(1L, LocalDateTime.now(), seller, buyer1, "메시지1"),
                true
        );
        final ReadChatRoomWithLastMessageDto dto2 = new ReadChatRoomWithLastMessageDto(
                2L,
                new ReadAuctionInChatRoomDto(2L, "경매2", 20_000, List.of(1L, 2L), "main", "sub", seller.id(), seller.profileImage(), seller.name(), seller.reliability()),
                buyer2,
                new ReadLastMessageDto(1L, LocalDateTime.now(), seller, buyer2, "메시지2"),
                true
        );

        given(chatRoomService.readAllByUserId(anyLong()))
                .willReturn(List.of(dto1, dto2));

        // when & then
        mockMvc.perform(get("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.[0].id", is(dto1.id()), Long.class),
                       jsonPath("$.[0].chatPartner.name", is(dto1.partnerDto().name())),
                       jsonPath("$.[0].auction.title", is(dto1.auctionDto().title())),
                       jsonPath("$.[0].lastMessage.contents", is(dto1.lastMessageDto().contents())),
                       jsonPath("$.[1].id", is(dto2.id()), Long.class),
                       jsonPath("$.[1].chatPartner.name", is(dto2.partnerDto().name())),
                       jsonPath("$.[1].auction.title", is(dto2.auctionDto().title())),
                       jsonPath("$.[1].lastMessage.contents", is(dto2.lastMessageDto().contents()))
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
    void 지정한_아이디에_해당하는_채팅방을_조회한다() throws Exception {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);

        final User seller = User.builder()
                                .name("판매자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("구매자")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();

        final Auction auction1 = Auction.builder()
                                        .title("경매 상품 1")
                                        .seller(seller)
                                        .subCategory(sub)
                                        .description("이것은 경매 상품 1 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
        auction1.addAuctionImages(List.of(new AuctionImage("사진", "image")));
        auction1.updateLastBid(new Bid(auction1, buyer, new BidPrice(3000)));

        final ReadParticipatingChatRoomDto chatRoom = new ReadParticipatingChatRoomDto(
                1L,
                ReadAuctionInChatRoomDto.from(auction1),
                ReadUserInChatRoomDto.from(seller),
                true
        );

        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willReturn(chatRoom);

        // when & then
        mockMvc.perform(get("/chattings/1")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.id", is(chatRoom.id()), Long.class),
                       jsonPath("$.chatPartner.name", is(chatRoom.partnerDto().name())),
                       jsonPath("$.auction.title", is(chatRoom.auctionDto().title()))
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_요청한_사용자_정보가_없다면_404를_반환한다() throws Exception {
        // given
        final Long invalidUserId = -999L;
        final UserNotFoundException userNotFoundException =
                new UserNotFoundException("사용자 정보를 찾을 수 없습니다.");
        given(chatRoomService.readByChatRoomId(anyLong(), anyLong()))
                .willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(get("/chattings/1")
                       .header(HttpHeaders.AUTHORIZATION, invalidUserId)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_채팅방을_찾을_수_없다면_404를_반환한다() throws Exception {
        // given
        final Long invalidChatRoomId = -999L;
        final ChatRoomNotFoundException chatRoomNotFoundException =
                new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");
        given(chatRoomService.readByChatRoomId(anyLong(), anyLong()))
                .willThrow(chatRoomNotFoundException);

        // when & then
        mockMvc.perform(get("/chattings/{chatRoomId}", invalidChatRoomId)
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(chatRoomNotFoundException.getMessage()))
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_요청한_사용자_채팅방의_참여자가_아니라면_404를_반환한다() throws Exception {
        // given
        final UserNotAccessibleException userNotAccessibleException =
                new UserNotAccessibleException("해당 채팅방에 접근할 권한이 없습니다.");
        given(chatRoomService.readByChatRoomId(anyLong(), anyLong()))
                .willThrow(userNotAccessibleException);

        // when & then
        mockMvc.perform(get("/chattings/1")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message", is(userNotAccessibleException.getMessage()))
               );
    }

    @Test
    void 채팅방을_생성한다() throws Exception {
        // given
        final Long newChatRoomId = 1L;
        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willReturn(newChatRoomId);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/chattings/" + newChatRoomId))
               );
    }

    @Test
    void 채팅방_생성시_요청한_사용자_정보를_찾을_수_없다면_404를_반환한다() throws Exception {
        // given
        final Long invalidUserId = -999L;
        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final UserNotFoundException userNotFoundException = new UserNotFoundException("사용자 정보를 찾을 수 없습니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class)))
                .willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, invalidUserId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 채팅방_생성시_관련된_경매_정보를_찾을_수_없다면_404를_반환한다() throws Exception {
        // given
        final Long invalidAuctionId = 999L;
        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(invalidAuctionId);
        final AuctionNotFoundException auctionNotFoundException =
                new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class)))
                .willThrow(auctionNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(auctionNotFoundException.getMessage()))
               );
    }

    @Test
    void 경매가_종료되지_않은_상태에서_채팅방을_생성하면_400을_반환한다() throws Exception {
        // given
        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final InvalidAuctionToChatException invalidAuctionToChatException =
                new InvalidAuctionToChatException("경매가 아직 종료되지 않았습니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class)))
                .willThrow(invalidAuctionToChatException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidAuctionToChatException.getMessage()))
               );
    }

    @Test
    void 경매가_삭제된_상태에서_채팅방을_생성하면_400을_반환한다() throws Exception {
        // given
        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final InvalidAuctionToChatException invalidAuctionToChatException =
                new InvalidAuctionToChatException("삭제된 경매입니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class)))
                .willThrow(invalidAuctionToChatException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidAuctionToChatException.getMessage()))
               );
    }

    @Test
    void 채팅방_생성시_낙찰자가_없다면_404를_반환한다() throws Exception {
        // given
        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final WinnerNotFoundException winnerNotFoundException =
                new WinnerNotFoundException("낙찰자가 존재하지 않습니다");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class)))
                .willThrow(winnerNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(winnerNotFoundException.getMessage()))
               );
    }

    @Test
    void 채팅방_생성을_요청한_사용자가_경매의_판매자_또는_최종_낙찰자가_아니라면_403을_반환한다() throws Exception {
        // given
        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final UserNotAccessibleException userNotAccessibleException =
                new UserNotAccessibleException("경매의 판매자 또는 최종 낙찰자만 채팅이 가능합니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class)))
                .willThrow(userNotAccessibleException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, 1L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message", is(userNotAccessibleException.getMessage()))
               );
    }
}
