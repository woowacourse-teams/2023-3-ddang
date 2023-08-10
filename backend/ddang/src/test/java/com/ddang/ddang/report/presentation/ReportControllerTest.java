package com.ddang.ddang.report.presentation;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.configuration.RestDocsConfiguration;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.report.application.AuctionReportService;
import com.ddang.ddang.report.application.ChatRoomReportService;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.application.dto.CreateChatRoomReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionInReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;
import com.ddang.ddang.report.application.dto.ReadChatRoomInReportDto;
import com.ddang.ddang.report.application.dto.ReadChatRoomReportDto;
import com.ddang.ddang.report.application.dto.ReadReporterDto;
import com.ddang.ddang.report.application.dto.ReadUserInReportDto;
import com.ddang.ddang.report.application.exception.AlreadyReportAuctionException;
import com.ddang.ddang.report.application.exception.AlreadyReportChatRoomException;
import com.ddang.ddang.report.application.exception.ChatRoomReportNotAccessibleException;
import com.ddang.ddang.report.application.exception.InvalidReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReporterToAuctionException;
import com.ddang.ddang.report.presentation.dto.request.CreateAuctionReportRequest;
import com.ddang.ddang.report.presentation.dto.request.CreateChatRoomReportRequest;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ReportController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.ddang\\.ddang\\.authentication\\.configuration\\..*")
        }
)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReportControllerTest {

    @MockBean
    AuctionReportService auctionReportService;

    @MockBean
    ChatRoomReportService chatRoomReportService;

    @Autowired
    ReportController reportController;

    @Autowired
    ObjectMapper objectMapper;

    TokenDecoder mockTokenDecoder;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockTokenDecoder = mock(TokenDecoder.class);

        final AuthenticationStore store = new AuthenticationStore();
        final AuthenticationInterceptor interceptor = new AuthenticationInterceptor(mockTokenDecoder, store);
        final AuthenticationPrincipalArgumentResolver resolver = new AuthenticationPrincipalArgumentResolver(store);

        mockMvc = MockMvcBuilders.standaloneSetup(reportController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver)
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void 경매_신고를_등록한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateAuctionReportRequest createAuctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionReportService.create(any(CreateAuctionReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/auctions")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createAuctionReportRequest))
               )
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/auctions/1"))
               );
    }

    @Test
    void 해당_사용자가_없는_경우_신고시_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateAuctionReportRequest createAuctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");
        final UserNotFoundException userNotFoundException = new UserNotFoundException("해당 사용자를 찾을 수 없습니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionReportService.create(any(CreateAuctionReportDto.class))).willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/reports/auctions")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createAuctionReportRequest))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 해당_경매가_없는_경우_신고시_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateAuctionReportRequest createAuctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");
        final AuctionNotFoundException auctionNotFoundException = new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionReportService.create(any(CreateAuctionReportDto.class))).willThrow(auctionNotFoundException);

        // when & then
        mockMvc.perform(post("/reports/auctions")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createAuctionReportRequest))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(auctionNotFoundException.getMessage()))
               );
    }

    @Test
    void 본인이_등록한_경매를_신고할_경우_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateAuctionReportRequest createAuctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");
        final InvalidReporterToAuctionException invalidReporterToAuctionException = new InvalidReporterToAuctionException("본인 경매글입니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionReportService.create(any(CreateAuctionReportDto.class))).willThrow(invalidReporterToAuctionException);

        // when & then
        mockMvc.perform(post("/reports/auctions")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createAuctionReportRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidReporterToAuctionException.getMessage()))
               );
    }

    @Test
    void 이미_삭제된_경매_신고시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateAuctionReportRequest createAuctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");
        final InvalidReportAuctionException invalidReportAuctionException = new InvalidReportAuctionException("이미 삭제된 경매입니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionReportService.create(any(CreateAuctionReportDto.class))).willThrow(invalidReportAuctionException);

        // when & then
        mockMvc.perform(post("/reports/auctions")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createAuctionReportRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidReportAuctionException.getMessage()))
               );
    }

    @Test
    void 이미_신고한_경매_신고시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateAuctionReportRequest createAuctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");
        final AlreadyReportAuctionException alreadyReportAuctionException = new AlreadyReportAuctionException("이미 신고한 경매입니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionReportService.create(any(CreateAuctionReportDto.class))).willThrow(alreadyReportAuctionException);

        // when & then
        mockMvc.perform(post("/reports/auctions")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createAuctionReportRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(alreadyReportAuctionException.getMessage()))
               );
    }

    @Test
    void 경매_아이디가_없는_경우_신고시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateAuctionReportRequest createAuctionReportRequest = new CreateAuctionReportRequest(null, "신고합니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionReportService.create(any(CreateAuctionReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/auctions")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createAuctionReportRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("경매 아이디가 입력되지 않았습니다."))
               );
    }

    @Test
    void 경매_아이디가_음수인_경우_신고시_400을_반환한다() throws Exception {
        // given
        final Long invalidAuctionId = -999L;
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateAuctionReportRequest createAuctionReportRequest = new CreateAuctionReportRequest(invalidAuctionId, "신고합니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionReportService.create(any(CreateAuctionReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/auctions")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createAuctionReportRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("경매 아이디는 양수여야 합니다."))
               );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 신고_내용_없이_경매_신고시_400을_반환한다(final String description) throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateAuctionReportRequest createAuctionReportRequest = new CreateAuctionReportRequest(1L, description);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionReportService.create(any(CreateAuctionReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/auctions")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createAuctionReportRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("신고 내용이 입력되지 않았습니다."))
               );
    }

    @Test
    void 전체_경매_신고_목록을_조회한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final ReadUserInReportDto userDto = new ReadUserInReportDto(1L, "판매자", "profile.png", 4.0d, "12345");
        final ReadAuctionReportDto auctionReportDto1 = new ReadAuctionReportDto(
                1L,
                new ReadReporterDto(1L, "회원1", "이미지1", 5.0),
                LocalDateTime.now(),
                new ReadAuctionInReportDto(1L, userDto, "제목", "설명", 100, 1_00, false, LocalDateTime.now()
                                                                                                   .plusDays(2), 2),
                "신고합니다."
        );
        final ReadAuctionReportDto auctionReportDto2 = new ReadAuctionReportDto(
                2L,
                new ReadReporterDto(2L, "회원2", "이미지2", 5.0),
                LocalDateTime.now(),
                new ReadAuctionInReportDto(1L, userDto, "제목", "설명", 100, 1_00, false, LocalDateTime.now()
                                                                                                   .plusDays(2), 2),
                "신고합니다."
        );
        final ReadAuctionReportDto auctionReportDto3 = new ReadAuctionReportDto(
                3L,
                new ReadReporterDto(3L, "회원3", "이미지3", 5.0),
                LocalDateTime.now(),
                new ReadAuctionInReportDto(1L, userDto, "제목", "설명", 100, 1_00, false, LocalDateTime.now()
                                                                                                   .plusDays(2), 2),
                "신고합니다."
        );
        given(auctionReportService.readAll())
                .willReturn(List.of(auctionReportDto1, auctionReportDto2, auctionReportDto3));

        // when & then
        mockMvc.perform(get("/reports/auctions")
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.reports.[0].id", is(auctionReportDto1.id()), Long.class),
                       jsonPath("$.reports.[0].reporter.id", is(auctionReportDto1.reporterDto().id()), Long.class),
                       jsonPath("$.reports.[0].reporter.name", is(auctionReportDto1.reporterDto().name())),
                       jsonPath("$.reports.[0].createdTime").exists(),
                       jsonPath("$.reports.[0].auction.id", is(auctionReportDto1.auctionDto().id()), Long.class),
                       jsonPath("$.reports.[0].auction.title", is(auctionReportDto1.auctionDto().title())),
                       jsonPath("$.reports.[0].description", is(auctionReportDto1.description())),
                       jsonPath("$.reports.[1].id", is(auctionReportDto2.id()), Long.class),
                       jsonPath("$.reports.[1].reporter.id", is(auctionReportDto2.reporterDto().id()), Long.class),
                       jsonPath("$.reports.[1].reporter.name", is(auctionReportDto2.reporterDto().name())),
                       jsonPath("$.reports.[1].createdTime").exists(),
                       jsonPath("$.reports.[1].auction.id", is(auctionReportDto2.auctionDto().id()), Long.class),
                       jsonPath("$.reports.[1].auction.title", is(auctionReportDto2.auctionDto().title())),
                       jsonPath("$.reports.[1].description", is(auctionReportDto2.description())),
                       jsonPath("$.reports.[2].id", is(auctionReportDto3.id()), Long.class),
                       jsonPath("$.reports.[2].reporter.id", is(auctionReportDto3.reporterDto().id()), Long.class),
                       jsonPath("$.reports.[2].reporter.name", is(auctionReportDto3.reporterDto().name())),
                       jsonPath("$.reports.[2].createdTime").exists(),
                       jsonPath("$.reports.[2].auction.id", is(auctionReportDto3.auctionDto().id()), Long.class),
                       jsonPath("$.reports.[2].auction.title", is(auctionReportDto3.auctionDto().title())),
                       jsonPath("$.reports.[2].description", is(auctionReportDto3.description()))
               );
    }

    @Test
    void 채팅방_신고를_등록한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateChatRoomReportRequest createChatRoomReportRequest = new CreateChatRoomReportRequest(1L, "신고합니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(chatRoomReportService.create(any(CreateChatRoomReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/chat-rooms")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createChatRoomReportRequest))
               )
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/chattings/1"))
               );
    }

    @Test
    void 존재하지_않은_사용자가_채팅방을_신고할시_404를_반환한다() throws Exception {
        // given
        final Long invalidUserId = -999L;
        final PrivateClaims privateClaims = new PrivateClaims(invalidUserId);
        final CreateChatRoomReportRequest createChatRoomReportRequest = new CreateChatRoomReportRequest(1L, "신고합니다");
        final UserNotFoundException userNotFoundException = new UserNotFoundException("해당 사용자를 찾을 수 없습니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(chatRoomReportService.create(any(CreateChatRoomReportDto.class))).willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/reports/chat-rooms")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createChatRoomReportRequest))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 존재하지_않은_채팅방을_신고할시_404를_반환한다() throws Exception {
        // given
        final Long invalidChatRoomId = 999L;
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateChatRoomReportRequest createChatRoomReportRequest = new CreateChatRoomReportRequest(invalidChatRoomId, "신고합니다");
        final ChatRoomNotFoundException chatRoomNotFoundException = new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(chatRoomReportService.create(any(CreateChatRoomReportDto.class))).willThrow(chatRoomNotFoundException);

        // when & then
        mockMvc.perform(post("/reports/chat-rooms")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createChatRoomReportRequest))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(chatRoomNotFoundException.getMessage()))
               );
    }

    @Test
    void 판매자_혹은_구매자가_아닌_사용자가_채팅방을_신고할시_403을_반환한다() throws Exception {
        // given
        final Long unaccessibleUserId = 999L;
        final PrivateClaims privateClaims = new PrivateClaims(unaccessibleUserId);
        final CreateChatRoomReportRequest createChatRoomReportRequest = new CreateChatRoomReportRequest(1L, "신고합니다");
        final ChatRoomReportNotAccessibleException chatRoomReportNotAccessibleException = new ChatRoomReportNotAccessibleException("해당 채팅방을 신고할 권한이 없습니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(chatRoomReportService.create(any(CreateChatRoomReportDto.class))).willThrow(chatRoomReportNotAccessibleException);

        // when & then
        mockMvc.perform(post("/reports/chat-rooms")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createChatRoomReportRequest))
               )
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message", is(chatRoomReportNotAccessibleException.getMessage()))
               );
    }

    @Test
    void 이미_신고한_사용자가_동일_채팅방을_신고할시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateChatRoomReportRequest createChatRoomReportRequest = new CreateChatRoomReportRequest(1L, "신고합니다");
        final AlreadyReportChatRoomException alreadyReportChatRoomException = new AlreadyReportChatRoomException("이미 신고한 채팅방입니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(chatRoomReportService.create(any(CreateChatRoomReportDto.class))).willThrow(alreadyReportChatRoomException);

        // when & then
        mockMvc.perform(post("/reports/chat-rooms")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createChatRoomReportRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(alreadyReportChatRoomException.getMessage()))
               );
    }

    @Test
    void 채팅방_아이디가_없는_경우_신고시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateChatRoomReportRequest createChatRoomReportRequest = new CreateChatRoomReportRequest(null, "신고합니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(chatRoomReportService.create(any(CreateChatRoomReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/chat-rooms")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createChatRoomReportRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("채팅방 아이디가 입력되지 않았습니다."))
               );
    }

    @Test
    void 채팅방_아이디가_음수인_경우_신고시_400을_반환한다() throws Exception {
        // given
        final Long invalidAuctionId = -999L;
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateChatRoomReportRequest createChatRoomReportRequest = new CreateChatRoomReportRequest(invalidAuctionId, "신고합니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(chatRoomReportService.create(any(CreateChatRoomReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/chat-rooms")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createChatRoomReportRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("채팅방 아이디는 양수여야 합니다."))
               );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 신고_내용_없이_채팅방_신고시_400을_반환한다(final String description) throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateChatRoomReportRequest createChatRoomReportRequest = new CreateChatRoomReportRequest(1L, description);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(chatRoomReportService.create(any(CreateChatRoomReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/chat-rooms")
                       .header("Authorization", "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createChatRoomReportRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("신고 내용이 입력되지 않았습니다."))
               );
    }

    @Test
    void 전체_채팅방_신고_목록을_조회한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        final ReadUserInReportDto sellerDto = new ReadUserInReportDto(1L, "판매자", "profile.png", 4.0d, "12345");
        final ReadAuctionInReportDto auctionInReportDto = new ReadAuctionInReportDto(
                1L,
                sellerDto,
                "제목",
                "설명",
                100,
                1_00,
                false,
                LocalDateTime.now().plusDays(2),
                2
        );
        final ReadUserInReportDto buyerDto1 = new ReadUserInReportDto(2L, "구매자1", "profile.png", 4.0d, "12346");
        final ReadChatRoomReportDto chatRoomReportDto1 = new ReadChatRoomReportDto(
                1L,
                new ReadReporterDto(1L, "회원1", "이미지1", 5.0),
                LocalDateTime.now(),
                new ReadChatRoomInReportDto(1L, auctionInReportDto, buyerDto1, false),
                "신고합니다."
        );
        final ReadUserInReportDto buyerDto2 = new ReadUserInReportDto(3L, "구매자2", "profile.png", 4.0d, "12347");
        final ReadChatRoomReportDto chatRoomReportDto2 = new ReadChatRoomReportDto(
                2L,
                new ReadReporterDto(1L, "회원1", "이미지1", 5.0),
                LocalDateTime.now(),
                new ReadChatRoomInReportDto(1L, auctionInReportDto, buyerDto2, false),
                "신고합니다."
        );
        final ReadUserInReportDto buyerDto3 = new ReadUserInReportDto(4L, "구매자3", "profile.png", 4.0d, "12348");
        final ReadChatRoomReportDto chatRoomReportDto3 = new ReadChatRoomReportDto(
                3L,
                new ReadReporterDto(1L, "회원1", "이미지1", 5.0),
                LocalDateTime.now(),
                new ReadChatRoomInReportDto(1L, auctionInReportDto, buyerDto3, false),
                "신고합니다."
        );
        given(chatRoomReportService.readAll())
                .willReturn(List.of(chatRoomReportDto1, chatRoomReportDto2, chatRoomReportDto3));

        // when & then
        mockMvc.perform(get("/reports/chat-rooms")
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.reports.[0].id", is(chatRoomReportDto1.id()), Long.class),
                       jsonPath("$.reports.[0].reporter.id", is(chatRoomReportDto1.reporterDto().id()), Long.class),
                       jsonPath("$.reports.[0].reporter.name", is(chatRoomReportDto1.reporterDto().name())),
                       jsonPath("$.reports.[0].createdTime").exists(),
                       jsonPath("$.reports.[0].chatRoom.id", is(chatRoomReportDto1.chatRoomDto().id()), Long.class),
                       jsonPath("$.reports.[0].description", is(chatRoomReportDto1.description())),
                       jsonPath("$.reports.[1].id", is(chatRoomReportDto2.id()), Long.class),
                       jsonPath("$.reports.[1].reporter.id", is(chatRoomReportDto2.reporterDto().id()), Long.class),
                       jsonPath("$.reports.[1].reporter.name", is(chatRoomReportDto2.reporterDto().name())),
                       jsonPath("$.reports.[1].createdTime").exists(),
                       jsonPath("$.reports.[1].chatRoom.id", is(chatRoomReportDto2.chatRoomDto().id()), Long.class),
                       jsonPath("$.reports.[1].description", is(chatRoomReportDto2.description())),
                       jsonPath("$.reports.[2].id", is(chatRoomReportDto3.id()), Long.class),
                       jsonPath("$.reports.[2].reporter.id", is(chatRoomReportDto3.reporterDto().id()), Long.class),
                       jsonPath("$.reports.[2].reporter.name", is(chatRoomReportDto3.reporterDto().name())),
                       jsonPath("$.reports.[2].createdTime").exists(),
                       jsonPath("$.reports.[2].chatRoom.id", is(chatRoomReportDto3.chatRoomDto().id()), Long.class),
                       jsonPath("$.reports.[2].description", is(chatRoomReportDto3.description()))
               );
    }
}
