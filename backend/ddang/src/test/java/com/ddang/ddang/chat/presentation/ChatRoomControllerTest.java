package com.ddang.ddang.chat.presentation;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
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
import com.ddang.ddang.chat.application.exception.InvalidUserToChat;
import com.ddang.ddang.chat.application.exception.MessageNotFoundException;
import com.ddang.ddang.chat.presentation.dto.request.CreateChatRoomRequest;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.chat.presentation.dto.request.ReadMessageRequest;
import com.ddang.ddang.chat.presentation.dto.response.ReadMessageResponse;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
class ChatRoomControllerTest extends CommonControllerSliceTest {

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
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 메시지를_생성한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final CreateMessageRequest request = new CreateMessageRequest(1L, "메시지 내용");

        given(messageService.create(any(CreateMessageDto.class), anyString())).willReturn(1L);

        // when & then
        final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/chattings/{chatRoomId}/messages", 1L)
                                                                                            .contentType(MediaType.APPLICATION_JSON)
                                                                                            .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                                                            .content(objectMapper.writeValueAsString(request)))
                                                   .andExpectAll(
                                                           status().isCreated(),
                                                           header().string(HttpHeaders.LOCATION, is("/chattings/1")),
                                                           jsonPath("$.id", is(1L), Long.class)
                                                   );
        createMessage_문서화(resultActions);
    }

    @Test
    void 채팅방이_없는_경우_메시지_생성시_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidChatRoomId = -999L;
        final CreateMessageRequest request = new CreateMessageRequest(1L, "메시지 내용");

        given(messageService.create(any(CreateMessageDto.class), anyString())).willThrow(new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", invalidChatRoomId)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is("지정한 아이디에 대한 채팅방을 찾을 수 없습니다."))
               );
    }

    @Test
    void 발신자가_없는_경우_메시지_생성시_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidWriterId = -999L;
        final Long chatRoomId = 1L;
        final CreateMessageRequest request = new CreateMessageRequest(invalidWriterId, "메시지 내용");

        given(messageService.create(any(CreateMessageDto.class), anyString())).willThrow(new UserNotFoundException("지정한 아이디에 대한 발신자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", chatRoomId)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is("지정한 아이디에 대한 발신자를 찾을 수 없습니다."))
               );
    }

    @Test
    void 마지막_조회_메시지_이후_메시지를_조회한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long lastMessageId = 1L;

        final ReadMessageDto readMessageDto = new ReadMessageDto(
                1L,
                LocalDateTime.now(),
                1L,
                1L,
                1L,
                "메시지내용"
        );
        final ReadMessageResponse expected = new ReadMessageResponse(1L, LocalDateTime.now(), true, "메시지내용");

        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willReturn(List.of(readMessageDto));

        // when & then
        final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/chattings/{chatRoomId}/messages", 1L)
                                                                                            .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                                                            .contentType(MediaType.APPLICATION_JSON)
                                                                                            .queryParam("lastMessageId", lastMessageId.toString())
                                                   )
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.[0].isMyMessage", is(expected.isMyMessage())),
                                                           jsonPath("$.[0].contents", is(expected.contents()))
                                                   );
        readAllByLastMessageId_문서화(resultActions);
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

        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willThrow(new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/chattings/" + invalidChatRoomId + "/messages")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .queryParam("lastMessageId", "1"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is("지정한 아이디에 대한 채팅방을 찾을 수 없습니다."))
               );
    }

    @Test
    void 마지막_메시지_아이디가_잘못된_경우_메시지를_조회하면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidMessageId = -999L;

        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willThrow(new MessageNotFoundException("조회한 마지막 메시지가 존재하지 않습니다."));

        // when & then
        mockMvc.perform(get("/chattings/1/messages")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .queryParam("lastMessageId", invalidMessageId.toString())
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is("조회한 마지막 메시지가 존재하지 않습니다."))
               );
    }

    @Test
    void 사용자가_참여한_모든_채팅방을_조회한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        ReadUserInChatRoomDto seller = new ReadUserInChatRoomDto(1L, "사용자1", 1L, 5.0d, false);
        final ReadUserInChatRoomDto buyer1 = new ReadUserInChatRoomDto(2L, "사용자2", 2L, 5.0d, false);
        final ReadUserInChatRoomDto buyer2 = new ReadUserInChatRoomDto(3L, "사용자3", 3L, 5.0d, false);
        final ReadAuctionInChatRoomDto auctionDto1 = new ReadAuctionInChatRoomDto(
                1L,
                "경매1",
                10_000,
                1L
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
                1L
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
        final ResultActions resultActions = mockMvc.perform(get("/chattings")
                                                           .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                           .contentType(MediaType.APPLICATION_JSON))
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.[0].id", is(dto1.id()), Long.class),
                                                           jsonPath("$.[0].chatPartner.name", is(dto1.partnerDto()
                                                                                                     .name())),
                                                           jsonPath("$.[0].auction.title", is(dto1.auctionDto()
                                                                                                  .title())),
                                                           jsonPath("$.[0].lastMessage.contents", is(dto1.lastMessageDto()
                                                                                                         .contents())),
                                                           jsonPath("$.[1].id", is(dto2.id()), Long.class),
                                                           jsonPath("$.[1].chatPartner.name", is(dto2.partnerDto()
                                                                                                     .name())),
                                                           jsonPath("$.[1].auction.title", is(dto2.auctionDto()
                                                                                                  .title())),
                                                           jsonPath("$.[1].lastMessage.contents", is(dto2.lastMessageDto()
                                                                                                         .contents()))
                                                   );
        readAllParticipatingChatRooms_문서화(resultActions);
    }

    @Test
    void 사용자가_참여한_채팅방_목록_조회시_요청한_사용자_정보가_없다면_404를_반환한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willThrow(new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));

        final Long invalidUserId = -999L;
        given(chatRoomService.readAllByUserId(invalidUserId)).willThrow(new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is("사용자 정보를 찾을 수 없습니다."))
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방을_조회한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final ReadAuctionInChatRoomDto auction = new ReadAuctionInChatRoomDto(
                1L,
                "경매 상품 1",
                3_000,
                1L
        );
        final ReadUserInChatRoomDto chatPartner = new ReadUserInChatRoomDto(
                2L,
                "채팅 상대방",
                2L,
                5.0,
                false
        );

        final ReadParticipatingChatRoomDto chatRoom = new ReadParticipatingChatRoomDto(
                1L,
                auction,
                chatPartner,
                true
        );

        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willReturn(chatRoom);

        // when & then
        final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/chattings/{chatRoomId}", 1L)
                                                                                            .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                                                            .contentType(MediaType.APPLICATION_JSON))
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.id", is(chatRoom.id()), Long.class),
                                                           jsonPath("$.chatPartner.name", is(chatRoom.partnerDto()
                                                                                                     .name())),
                                                           jsonPath("$.auction.title", is(chatRoom.auctionDto()
                                                                                                  .title()))
                                                   );
        readChatRoom_문서화(resultActions);
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_요청한_사용자_정보가_없다면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willThrow(new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/chattings/1")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is("사용자 정보를 찾을 수 없습니다."))
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_채팅방을_찾을_수_없다면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidChatRoomId = -999L;

        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willThrow(new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/chattings/{chatRoomId}", invalidChatRoomId)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is("지정한 아이디에 대한 채팅방을 찾을 수 없습니다."))
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_요청한_사용자_채팅방의_참여자가_아니라면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final InvalidUserToChat invalidUserToChat =
                new InvalidUserToChat("해당 채팅방에 접근할 권한이 없습니다.");

        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willThrow(invalidUserToChat);

        // when & then
        mockMvc.perform(get("/chattings/1")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message", is(invalidUserToChat.getMessage()))
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
        final ResultActions resultActions = mockMvc.perform(post("/chattings")
                                                           .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(objectMapper.writeValueAsString(chatRoomRequest)))
                                                   .andExpectAll(
                                                           status().isCreated(),
                                                           header().string(HttpHeaders.LOCATION, is("/chattings/" + newChatRoomId))
                                                   );
        createChatRoom_문서화(resultActions);
    }

    @Test
    void 채팅방_생성시_요청한_사용자_정보를_찾을_수_없다면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is("사용자 정보를 찾을 수 없습니다."))
               );
    }

    @Test
    void 채팅방_생성시_관련된_경매_정보를_찾을_수_없다면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final Long invalidAuctionId = 999L;
        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(invalidAuctionId);

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is("해당 경매를 찾을 수 없습니다."))
               );
    }

    @Test
    void 경매가_종료되지_않은_상태에서_채팅방을_생성하면_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(new InvalidAuctionToChatException("경매가 아직 종료되지 않았습니다."));

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("경매가 아직 종료되지 않았습니다."))
               );
    }

    @Test
    void 채팅방_생성시_낙찰자가_없다면_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(new WinnerNotFoundException("낙찰자가 존재하지 않습니다"));

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is("낙찰자가 존재하지 않습니다"))
               );
    }

    @Test
    void 채팅방_생성을_요청한_사용자가_경매의_판매자_또는_최종_낙찰자가_아니라면_403을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final CreateChatRoomRequest chatRoomRequest = new CreateChatRoomRequest(1L);
        final InvalidUserToChat invalidUserToChat =
                new InvalidUserToChat("경매의 판매자 또는 최종 낙찰자만 채팅이 가능합니다.");

        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(invalidUserToChat);

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(chatRoomRequest)))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message", is(invalidUserToChat.getMessage()))
               );
    }

    private void createMessage_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        pathParameters(
                                parameterWithName("chatRoomId").description("메시지를 보내고 싶은 채팅방의 ID")
                        ),
                        requestFields(
                                fieldWithPath("receiverId").description("메시지 수신자 ID"),
                                fieldWithPath("contents").description("메시지 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                   .description("메시지 보내진 채팅방 ID")
                        )
                )
        );
    }

    private void readAllByLastMessageId_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        pathParameters(
                                parameterWithName("chatRoomId").description("메시지를 보내고 싶은 채팅방의 ID")
                        ),
                        queryParameters(
                                parameterWithName("lastMessageId").description("마지막으로 응답받은 메시지의 ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                   .description("하나의 채팅방 내의 메시지 목록 (lastMessageId가 포함되어 있다면 lastMessageId 이후의 메시지 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("메시지 ID"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                                             .description("메시지를 보낸 시간"),
                                fieldWithPath("[].isMyMessage").type(JsonFieldType.BOOLEAN)
                                                               .description("조회를 요청한 사람이 보낸 메시지인지 여부"),
                                fieldWithPath("[].contents").type(JsonFieldType.STRING)
                                                            .description("메시지 내용")
                        )
                )
        );
    }

    private void readAllParticipatingChatRooms_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("자신이 참여한 채팅방 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("채팅방 ID"),
                                fieldWithPath("[].chatPartner").type(JsonFieldType.OBJECT).description("채팅 상대방"),
                                fieldWithPath("[].chatPartner.id").type(JsonFieldType.NUMBER)
                                                                  .description("채팅 상대방 ID"),
                                fieldWithPath("[].chatPartner.name").type(JsonFieldType.STRING)
                                                                    .description("채팅 상대방 이름"),
                                fieldWithPath("[].chatPartner.profileImage").type(JsonFieldType.STRING)
                                                                            .description("채팅 상대방 프로필 사진"),
                                fieldWithPath("[].auction").type(JsonFieldType.OBJECT)
                                                           .description("채팅방과 연관된 경매"),
                                fieldWithPath("[].auction.id").type(JsonFieldType.NUMBER).description("경매 ID"),
                                fieldWithPath("[].auction.title").type(JsonFieldType.STRING)
                                                                 .description("경매 제목"),
                                fieldWithPath("[].auction.image").type(JsonFieldType.STRING)
                                                                 .description("경매 대표 사진"),
                                fieldWithPath("[].auction.price").type(JsonFieldType.NUMBER).description("낙찰가"),
                                fieldWithPath("[].lastMessage").type(JsonFieldType.OBJECT)
                                                               .description("마지막으로 전송된 메시지"),
                                fieldWithPath("[].lastMessage.createdAt").type(JsonFieldType.STRING)
                                                                         .description("메시지를 보낸 시간"),
                                fieldWithPath("[].lastMessage.contents").type(JsonFieldType.STRING)
                                                                        .description("메시지 내용"),
                                fieldWithPath("[].isChatAvailable").type(JsonFieldType.BOOLEAN)
                                                                   .description("채팅 가능 여부")
                        )
                )
        );
    }

    private void readChatRoom_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        pathParameters(
                                parameterWithName("chatRoomId").description("조회하고자 하는 채팅방 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("채팅방 ID"),
                                fieldWithPath("auction").type(JsonFieldType.OBJECT).description("채팅방과 연관된 경매"),
                                fieldWithPath("auction.id").type(JsonFieldType.NUMBER).description("경매 ID"),
                                fieldWithPath("auction.title").type(JsonFieldType.STRING).description("경매 제목"),
                                fieldWithPath("auction.image").type(JsonFieldType.STRING)
                                                              .description("경매 대표 사진"),
                                fieldWithPath("auction.price").type(JsonFieldType.NUMBER).description("낙찰가"),
                                fieldWithPath("chatPartner").type(JsonFieldType.OBJECT).description("채팅 상대방"),
                                fieldWithPath("chatPartner.id").type(JsonFieldType.NUMBER)
                                                               .description("채팅 상대방 ID"),
                                fieldWithPath("chatPartner.name").type(JsonFieldType.STRING)
                                                                 .description("채팅 상대방 이름"),
                                fieldWithPath("chatPartner.profileImage").type(JsonFieldType.STRING)
                                                                         .description("채팅 상대방 프로필 사진"),
                                fieldWithPath("isChatAvailable").type(JsonFieldType.BOOLEAN)
                                                                .description("채팅 가능 여부")
                        )
                )
        );
    }

    private void createChatRoom_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        requestFields(
                                fieldWithPath("auctionId").type(JsonFieldType.NUMBER)
                                                          .description("연관된 경매 ID")
                        ),
                        responseFields(
                                fieldWithPath("chatRoomId").type(JsonFieldType.NUMBER)
                                                           .description("생성된 채팅방 ID")
                        )
                )
        );
    }
}
