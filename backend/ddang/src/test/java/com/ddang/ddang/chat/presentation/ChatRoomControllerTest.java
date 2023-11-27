package com.ddang.ddang.chat.presentation;

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

import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.auction.infrastructure.persistence.exception.AuctionNotFoundException;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.configuration.AuthenticationStore;
import com.ddang.ddang.chat.application.dto.request.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.request.CreateMessageDto;
import com.ddang.ddang.chat.application.exception.InvalidAuctionToChatException;
import com.ddang.ddang.chat.application.exception.ForbiddenChattingUserException;
import com.ddang.ddang.chat.application.exception.MessageNotFoundException;
import com.ddang.ddang.chat.application.exception.UnableToChatException;
import com.ddang.ddang.chat.infrastructure.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.presentation.dto.request.ReadMessageRequest;
import com.ddang.ddang.chat.presentation.dto.response.ReadMessageResponse;
import com.ddang.ddang.chat.presentation.fixture.ChatRoomControllerFixture;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.user.infrastructure.exception.UserNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

@SuppressWarnings("NonAsciiCharacters")
class ChatRoomControllerTest extends ChatRoomControllerFixture {

    TokenDecoder tokenDecoder;

    MockMvc mockMvc;

    ChatRoomController chatRoomController;

    @BeforeEach
    void setUp() {
        tokenDecoder = mock(TokenDecoder.class);

        final AuthenticationStore store = new AuthenticationStore();
        final AuthenticationInterceptor interceptor = new AuthenticationInterceptor(
                blackListTokenService,
                authenticationUserService,
                tokenDecoder,
                store
        );
        final AuthenticationPrincipalArgumentResolver resolver = new AuthenticationPrincipalArgumentResolver(store);

        chatRoomController = new ChatRoomController(chatRoomService, messageService, urlFinder);
        mockMvc = MockMvcBuilders.standaloneSetup(chatRoomController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver)
                                 .setMessageConverters(mappingJackson2HttpMessageConverter)
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 메시지를_생성한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(messageService.create(any(CreateMessageDto.class), anyString())).willReturn(채팅방_아이디);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                                                           RestDocumentationRequestBuilders.post("/chattings/{chatRoomId}/messages", 채팅방_아이디)
                                                                                           .contentType(MediaType.APPLICATION_JSON)
                                                                                           .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                                                           .content(objectMapper.writeValueAsString(메시지_생성_요청)))
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
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(messageService.create(any(CreateMessageDto.class), anyString())).willThrow(
                new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", 유효하지_않은_채팅방_아이디)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .content(objectMapper.writeValueAsString(메시지_생성_요청))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 발신자가_없는_경우_메시지_생성시_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(messageService.create(any(CreateMessageDto.class), anyString())).willThrow(
                new UserNotFoundException("지정한 아이디에 대한 발신자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", 채팅방_아이디)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .content(objectMapper.writeValueAsString(유효하지_않은_발신자의_메시지_생성_요청))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 발신자가_탈퇴한_사용자인_경우_메시지_생성시_400를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(messageService.create(any(CreateMessageDto.class), anyString())).willThrow(
                new UnableToChatException("탈퇴한 사용자에게는 메시지 전송이 불가능합니다."));

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", 채팅방_아이디)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .content(objectMapper.writeValueAsString(탈퇴한_사용자와의_메시지_생성_요청))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 마지막_조회_메시지_이후_메시지를_조회한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willReturn(List.of(조회용_메시지));

        final ReadMessageResponse expected = ReadMessageResponse.of(조회용_메시지, true);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                                                           RestDocumentationRequestBuilders.get("/chattings/{chatRoomId}/messages", 채팅방_아이디)
                                                                                           .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                                                           .contentType(MediaType.APPLICATION_JSON)
                                                                                           .queryParam("lastMessageId", 마지막_메시지_아이디.toString())
                                                   )
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.[0].isMyMessage", is(expected.isMyMessage())),
                                                           jsonPath("$.[0].content", is(expected.content()))
                                                   );
        readAllByLastMessageId_문서화(resultActions);
    }

    @Test
    void 마지막_메시지_아이디가_없는_경우_빈_리스트를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/chattings/{chatRoomId}/messages", 채팅방_아이디)
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
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willThrow(
                new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/chattings/{chatRoomId}/messages", 유효하지_않은_채팅방_아이디)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .queryParam("lastMessageId", 마지막_메시지_아이디.toString()))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 마지막_메시지_아이디가_잘못된_경우_메시지를_조회하면_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(messageService.readAllByLastMessageId(any(ReadMessageRequest.class))).willThrow(
                new MessageNotFoundException("조회한 마지막 메시지가 존재하지 않습니다."));

        // when & then
        mockMvc.perform(get("/chattings/{chatRoomId}/messages", 채팅방_아이디)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .queryParam("lastMessageId", 유효하지_않은_마지막_메시지_아이디.toString())
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 사용자가_참여한_모든_채팅방을_조회한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.readAllByUserId(anyLong())).willReturn(List.of(조회용_채팅방1, 조회용_채팅방2));

        // when & then
        final ResultActions resultActions = mockMvc.perform(get("/chattings")
                                                           .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                           .contentType(MediaType.APPLICATION_JSON))
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.[0].id", is(조회용_채팅방1.chatRoomId()), Long.class),
                                                           jsonPath("$.[0].partner.name", is(조회용_채팅방1.partnerDto()
                                                                                                     .name())),
                                                           jsonPath("$.[0].auction.title", is(조회용_채팅방1.auctionDto()
                                                                                                      .title())),
                                                           jsonPath("$.[0].lastMessage.content",
                                                                   is(조회용_채팅방1.lastMessageDto()
                                                                              .content())),
                                                           jsonPath("$.[0].unreadMessageCount",
                                                                   is(조회용_채팅방1.unreadMessageCount()), Long.class),
                                                           jsonPath("$.[1].id", is(조회용_채팅방2.chatRoomId()), Long.class),
                                                           jsonPath("$.[1].partner.name", is(조회용_채팅방2.partnerDto()
                                                                                                     .name())),
                                                           jsonPath("$.[1].auction.title", is(조회용_채팅방2.auctionDto()
                                                                                                      .title())),
                                                           jsonPath("$.[1].lastMessage.content",
                                                                   is(조회용_채팅방2.lastMessageDto()
                                                                              .content())),
                                                           jsonPath("$.[1].unreadMessageCount",
                                                                   is(조회용_채팅방1.unreadMessageCount()), Long.class)
                                                   );
        readAllParticipatingChatRooms_문서화(resultActions);
    }

    @Test
    void 사용자가_참여한_채팅방_목록_조회시_요청한_사용자_정보가_없다면_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willThrow(
                new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방을_조회한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willReturn(조회용_참가중인_채팅방);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                                                           RestDocumentationRequestBuilders.get("/chattings/{chatRoomId}", 채팅방_아이디)
                                                                                           .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                                                           .contentType(MediaType.APPLICATION_JSON))
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.id", is(조회용_참가중인_채팅방.id()), Long.class),
                                                           jsonPath("$.auction.title",
                                                                   is(조회용_참가중인_채팅방.auctionDto().title())),
                                                           jsonPath("$.partner.name",
                                                                   is(조회용_참가중인_채팅방.partnerDto().name()))
                                                   );
        readChatRoom_문서화(resultActions);
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_요청한_사용자_정보가_없다면_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willThrow(
                new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/chattings/{chatRoomId}", 채팅방_아이디)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_채팅방을_찾을_수_없다면_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willThrow(
                new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/chattings/{chatRoomId}", 유효하지_않은_채팅방_아이디)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_요청한_사용자_채팅방의_참여자가_아니라면_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.readByChatRoomId(anyLong(), anyLong())).willThrow(
                new ForbiddenChattingUserException("해당 채팅방에 접근할 권한이 없습니다."));

        // when & then
        mockMvc.perform(get("/chattings/{chatRoomId}", 채팅방_아이디)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 채팅방을_생성한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willReturn(채팅방_아이디);

        // when & then
        final ResultActions resultActions = mockMvc.perform(post("/chattings")
                                                           .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(objectMapper.writeValueAsString(채팅방_생성_요청)))
                                                   .andExpectAll(
                                                           status().isCreated(),
                                                           header().string(HttpHeaders.LOCATION,
                                                                   is("/chattings/" + 채팅방_아이디))
                                                   );
        createChatRoom_문서화(resultActions);
    }

    @Test
    void 채팅방_생성시_요청한_사용자_정보를_찾을_수_없다면_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(
                new UserNotFoundException("사용자 정보를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(채팅방_생성_요청)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 채팅방_생성시_관련된_경매_정보를_찾을_수_없다면_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(
                new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(존재하지_않은_경매_아이디_채팅방_생성_요청)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 채팅방_생성시_유효하지_않은_경매_아이디를_전달받는다면_400를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(유효하지_않은_경매_아이디_채팅방_생성_요청)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매가_종료되지_않은_상태에서_채팅방을_생성하면_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(
                new InvalidAuctionToChatException("경매가 아직 종료되지 않았습니다."));

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(채팅방_생성_요청)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 채팅방_생성시_낙찰자가_없다면_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(
                new WinnerNotFoundException("낙찰자가 존재하지 않습니다"));

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(채팅방_생성_요청)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 채팅방_생성을_요청한_사용자가_경매의_판매자_또는_최종_낙찰자가_아니라면_403을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(chatRoomService.create(anyLong(), any(CreateChatRoomDto.class))).willThrow(
                new ForbiddenChattingUserException("경매의 판매자 또는 최종 낙찰자만 채팅이 가능합니다."));

        // when & then
        mockMvc.perform(post("/chattings")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(채팅방_생성_요청)))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message").exists()
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
                                fieldWithPath("content").description("메시지 내용")
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
                                                   .description(
                                                           "하나의 채팅방 내의 메시지 목록 (lastMessageId가 포함되어 있다면 lastMessageId 이후의 메시지 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("메시지 ID"),
                                fieldWithPath("[].createdTime").type(JsonFieldType.STRING)
                                                               .description("메시지를 보낸 시간"),
                                fieldWithPath("[].isMyMessage").type(JsonFieldType.BOOLEAN)
                                                               .description("조회를 요청한 사람이 보낸 메시지인지 여부"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING)
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
                                fieldWithPath("[].partner").type(JsonFieldType.OBJECT).description("채팅 상대방"),
                                fieldWithPath("[].partner.id").type(JsonFieldType.NUMBER)
                                                              .description("채팅 상대방 ID"),
                                fieldWithPath("[].partner.name").type(JsonFieldType.STRING)
                                                                .description("채팅 상대방 이름"),
                                fieldWithPath("[].partner.profileImage").type(JsonFieldType.STRING)
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
                                fieldWithPath("[].lastMessage.createdTime").type(JsonFieldType.STRING)
                                                                           .description("메시지를 보낸 시간"),
                                fieldWithPath("[].lastMessage.content").type(JsonFieldType.STRING)
                                                                       .description("메시지 내용"),
                                fieldWithPath("[].unreadMessageCount").type(JsonFieldType.NUMBER)
                                                                      .description("안 읽은 메시지 개수"),
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
                                fieldWithPath("partner").type(JsonFieldType.OBJECT).description("채팅 상대방"),
                                fieldWithPath("partner.id").type(JsonFieldType.NUMBER)
                                                           .description("채팅 상대방 ID"),
                                fieldWithPath("partner.name").type(JsonFieldType.STRING)
                                                             .description("채팅 상대방 이름"),
                                fieldWithPath("partner.profileImage").type(JsonFieldType.STRING)
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
