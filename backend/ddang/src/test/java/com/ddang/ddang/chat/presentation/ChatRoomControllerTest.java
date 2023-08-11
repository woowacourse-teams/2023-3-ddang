package com.ddang.ddang.chat.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.authentication.application.AuthenticationUserService;
import com.ddang.ddang.authentication.application.BlackListTokenService;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
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
import com.ddang.ddang.chat.application.dto.ReadMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadUserInChatRoomDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.InvalidAuctionToChatException;
import com.ddang.ddang.chat.application.exception.MessageNotFoundException;
import com.ddang.ddang.chat.application.exception.UserNotAccessibleException;
import com.ddang.ddang.chat.presentation.dto.request.CreateChatRoomRequest;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.chat.presentation.dto.request.ReadMessageRequest;
import com.ddang.ddang.chat.presentation.dto.response.ReadMessageResponse;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    BlackListTokenService blackListTokenService;

    @MockBean
    ChatRoomService chatRoomService;

    @MockBean
    MessageService messageService;

    @MockBean
    AuthenticationUserService authenticationUserService;

    @Autowired
    ChatRoomController chatRoomController;

    @Autowired
    ObjectMapper objectMapper;

    TokenDecoder mockTokenDecoder;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockTokenDecoder = mock(TokenDecoder.class);

        final AuthenticationStore store = new AuthenticationStore();
        final AuthenticationInterceptor interceptor = new AuthenticationInterceptor(
                blackListTokenService,
                authenticationUserService,
                mockTokenDecoder, 
                store
        );
        final AuthenticationPrincipalArgumentResolver resolver = new AuthenticationPrincipalArgumentResolver(store);

        mockMvc = MockMvcBuilders.standaloneSetup(chatRoomController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver)
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void 메시지를_생성한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final String contents = "메시지 내용";
        final CreateMessageRequest request = new CreateMessageRequest(
                1L,
                contents
        );

        given(messageService.create(any(CreateMessageDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/chattings/1/messages")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/chattings/1")),
                       jsonPath("$.id", is(1L), Long.class)
               );
    }

    @Test
    void 채팅방이_없는_경우_메시지_생성시_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidChatRoomId = -999L;
        final String contents = "메시지 내용";
        final CreateMessageRequest request = new CreateMessageRequest(1L, contents);

        final ChatRoomNotFoundException chatRoomNotFoundException =
                new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");
        given(messageService.create(any(CreateMessageDto.class))).willThrow(chatRoomNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", invalidChatRoomId)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidWriterId = -999L;
        final Long chatRoomId = 1L;
        final String contents = "메시지 내용";
        final CreateMessageRequest request = new CreateMessageRequest(invalidWriterId, contents);

        final UserNotFoundException userNotFoundException = new UserNotFoundException(
                "지정한 아이디에 대한 발신자를 찾을 수 없습니다."
        );
        given(messageService.create(any(CreateMessageDto.class))).willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", chatRoomId)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 마지막_조회_메시지_이후_메시지를_조회한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long lastMessageId = 1L;
        final User user = User.builder()
                              .name("상대1")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        final ReadAuctionInChatRoomDto readAuctionDto = new ReadAuctionInChatRoomDto(
                1L,
                "경매1",
                10_000,
                List.of(1L, 2L),
                "main",
                "sub",
                user.getId(),
                user.getProfileImage(),
                user.getName(),
                user.getReliability()
        );

        final ReadParticipatingChatRoomDto chatRoomDto = new ReadParticipatingChatRoomDto(
                1L,
                readAuctionDto,
                ReadUserInChatRoomDto.from(user),
                true
        );
        final ReadUserInChatRoomDto readWriterDto = new ReadUserInChatRoomDto(1L, "user", "profile.png", 5.0d);
        final ReadUserInChatRoomDto readReceiverDto = new ReadUserInChatRoomDto(1L, "user", "profile.png", 5.0d);
        final ReadMessageDto readMessageDto = new ReadMessageDto(
                1L,
                LocalDateTime.now(),
                chatRoomDto,
                readWriterDto,
                readReceiverDto,
                "메시지내용"
        );
        final ReadMessageResponse expected = new ReadMessageResponse(1L, LocalDateTime.now(), true, "메시지내용");

        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willReturn(List.of(readMessageDto));

        // when & then
        mockMvc.perform(get("/chattings/1/messages")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .queryParam("lastMessageId", lastMessageId.toString())
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.[0].isMyMessage", is(expected.isMyMessage())),
                       jsonPath("$.[0].contents", is(expected.contents()))
               );
    }

    @Test
    void 마지막_메시지_아이디가_없는_경우_빈_리스트를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/chattings/1/messages")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isOk(),
                       content().json("[]")
               );
    }

    @Test
    void 채팅방_아이디가_잘못된_경우_메시지를_조회하면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidChatRoomId = -999L;
        final ChatRoomNotFoundException chatRoomNotFoundException =
                new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");

        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willThrow(chatRoomNotFoundException);

        // when & then
        mockMvc.perform(get("/chattings/" + invalidChatRoomId + "/messages")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .queryParam("lastMessageId", "1"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(chatRoomNotFoundException.getMessage()))
               );
    }

    @Test
    void 마지막_메시지_아이디가_잘못된_경우_메시지를_조회하면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidMessageId = -999L;
        final MessageNotFoundException messageNotFoundException =
                new MessageNotFoundException("조회한 마지막 메시지가 존재하지 않습니다.");

        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willThrow(messageNotFoundException);

        // when & then
        mockMvc.perform(get("/chattings/1/messages")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .queryParam("lastMessageId", invalidMessageId.toString())
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(messageNotFoundException.getMessage()))
               );
    }

    @Test
    void 사용자가_참여한_모든_채팅방을_조회한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        ReadUserInChatRoomDto seller = new ReadUserInChatRoomDto(1L, "사용자1", "profile.png", 5.0d);
        final ReadUserInChatRoomDto buyer1 = new ReadUserInChatRoomDto(2L, "사용자2", "profile.png", 5.0d);
        final ReadUserInChatRoomDto buyer2 = new ReadUserInChatRoomDto(3L, "사용자3", "profile.png", 5.0d);
        final ReadAuctionInChatRoomDto auctionDto1 = new ReadAuctionInChatRoomDto(
                1L,
                "경매1",
                10_000,
                List.of(1L, 2L),
                "main",
                "sub",
                seller.id(),
                seller.profileImage(),
                seller.name(),
                seller.reliability()
        );
        final ReadChatRoomWithLastMessageDto dto1 = new ReadChatRoomWithLastMessageDto(
                1L,
                auctionDto1,
                buyer1,
                new ReadLastMessageDto(1L, LocalDateTime.now(), seller, buyer1, "메시지1"),
                true
        );
        final ReadAuctionInChatRoomDto auctionDto2 = new ReadAuctionInChatRoomDto(
                2L,
                "경매2",
                20_000,
                List.of(1L, 2L),
                "main",
                "sub",
                seller.id(),
                seller.profileImage(),
                seller.name(),
                seller.reliability()
        );
        final ReadChatRoomWithLastMessageDto dto2 = new ReadChatRoomWithLastMessageDto(
                2L,
                auctionDto2,
                buyer2,
                new ReadLastMessageDto(1L, LocalDateTime.now(), seller, buyer2, "메시지2"),
                true
        );

        given(chatRoomService.readAllByUserId(anyLong()))
                .willReturn(List.of(dto1, dto2));

        // when & then
        mockMvc.perform(get("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final UserNotFoundException userNotFoundException = new UserNotFoundException("사용자 정보를 찾을 수 없습니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willThrow(userNotFoundException);

        final Long invalidUserId = -999L;
        given(chatRoomService.readAllByUserId(invalidUserId)).willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(get("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방을_조회한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

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
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final UserNotFoundException userNotFoundException = new UserNotFoundException("사용자 정보를 찾을 수 없습니다.");
        
        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(get("/chattings/1")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_채팅방을_찾을_수_없다면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidChatRoomId = -999L;
        final ChatRoomNotFoundException chatRoomNotFoundException =
                new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");
        
        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willThrow(chatRoomNotFoundException);

        // when & then
        mockMvc.perform(get("/chattings/{chatRoomId}", invalidChatRoomId)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(chatRoomNotFoundException.getMessage()))
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_요청한_사용자_채팅방의_참여자가_아니라면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final UserNotAccessibleException userNotAccessibleException = 
                new UserNotAccessibleException("해당 채팅방에 접근할 권한이 없습니다.");
        
        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willThrow(userNotAccessibleException);

        // when & then
        mockMvc.perform(get("/chattings/1")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message", is(userNotAccessibleException.getMessage()))
               );
    }

    @Test
    void 채팅방을_생성한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long newChatRoomId = 1L;
        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willReturn(newChatRoomId);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final UserNotFoundException userNotFoundException = new UserNotFoundException("사용자 정보를 찾을 수 없습니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidAuctionId = 999L;
        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(invalidAuctionId);
        final AuctionNotFoundException auctionNotFoundException = new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(auctionNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final InvalidAuctionToChatException invalidAuctionToChatException = 
                new InvalidAuctionToChatException("경매가 아직 종료되지 않았습니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(invalidAuctionToChatException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final InvalidAuctionToChatException invalidAuctionToChatException =
                new InvalidAuctionToChatException("삭제된 경매입니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(invalidAuctionToChatException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final WinnerNotFoundException winnerNotFoundException = new WinnerNotFoundException("낙찰자가 존재하지 않습니다");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(winnerNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final UserNotAccessibleException userNotAccessibleException =
                new UserNotAccessibleException("경매의 판매자 또는 최종 낙찰자만 채팅이 가능합니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(userNotAccessibleException);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message", is(userNotAccessibleException.getMessage()))
               );
    }
}
