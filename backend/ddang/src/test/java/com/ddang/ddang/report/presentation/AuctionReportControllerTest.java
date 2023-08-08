package com.ddang.ddang.report.presentation;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.application.exception.UserNotFoundException;
import com.ddang.ddang.bid.presentation.resolver.LoginUserArgumentResolver;
import com.ddang.ddang.configuration.RestDocsConfiguration;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.report.application.AuctionReportService;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.application.exception.AlreadyReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReporterToAuctionException;
import com.ddang.ddang.report.presentation.dto.CreateAuctionReportRequest;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuctionReportController.class})
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionReportControllerTest {

    @MockBean
    AuctionReportService auctionReportService;

    @Autowired
    AuctionReportController auctionReportController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auctionReportController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .setCustomArgumentResolvers(new LoginUserArgumentResolver())
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void 입찰을_등록한다() throws Exception {
        // given
        final CreateAuctionReportRequest auctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");

        given(auctionReportService.create(any(LoginUserDto.class), any(CreateAuctionReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/auctions").header("Authorization", 1L)
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(auctionReportRequest)))
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/auctions/1"))
               );
    }

    @Test
    void 해당_사용자가_없는_경우_신고시_404를_반환한다() throws Exception {
        // given
        final CreateAuctionReportRequest auctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");
        final UserNotFoundException userNotFoundException = new UserNotFoundException("해당 사용자를 찾을 수 없습니다.");

        given(auctionReportService.create(any(LoginUserDto.class), any(CreateAuctionReportDto.class)))
                .willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/reports/auctions").header("Authorization", 1L)
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(auctionReportRequest)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 해당_경매가_없는_경우_신고시_404를_반환한다() throws Exception {
        // given
        final CreateAuctionReportRequest auctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");
        final AuctionNotFoundException auctionNotFoundException = new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");

        given(auctionReportService.create(any(LoginUserDto.class), any(CreateAuctionReportDto.class)))
                .willThrow(auctionNotFoundException);

        // when & then
        mockMvc.perform(post("/reports/auctions").header("Authorization", 1L)
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(auctionReportRequest)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(auctionNotFoundException.getMessage()))
               );
    }

    @Test
    void 본인이_등록한_경매를_신고할_경우_400을_반환한다() throws Exception {
        // given
        final CreateAuctionReportRequest auctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");
        final InvalidReporterToAuctionException invalidReporterToAuctionException = new InvalidReporterToAuctionException("본인 경매글입니다.");

        given(auctionReportService.create(any(LoginUserDto.class), any(CreateAuctionReportDto.class)))
                .willThrow(invalidReporterToAuctionException);

        // when & then
        mockMvc.perform(post("/reports/auctions").header("Authorization", 1L)
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(auctionReportRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidReporterToAuctionException.getMessage()))
               );
    }

    @Test
    void 이미_삭제된_경매_신고시_400을_반환한다() throws Exception {
        // given
        final CreateAuctionReportRequest auctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");
        final InvalidReportAuctionException invalidReportAuctionException = new InvalidReportAuctionException("이미 삭제된 경매입니다.");

        given(auctionReportService.create(any(LoginUserDto.class), any(CreateAuctionReportDto.class)))
                .willThrow(invalidReportAuctionException);

        // when & then
        mockMvc.perform(post("/reports/auctions").header("Authorization", 1L)
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(auctionReportRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidReportAuctionException.getMessage()))
               );
    }

    @Test
    void 이미_신고한_경매_신고시_400을_반환한다() throws Exception {
        // given
        final CreateAuctionReportRequest auctionReportRequest = new CreateAuctionReportRequest(1L, "신고합니다");
        final AlreadyReportAuctionException alreadyReportAuctionException = new AlreadyReportAuctionException("이미 신고한 경매입니다.");

        given(auctionReportService.create(any(LoginUserDto.class), any(CreateAuctionReportDto.class)))
                .willThrow(alreadyReportAuctionException);

        // when & then
        mockMvc.perform(post("/reports/auctions").header("Authorization", 1L)
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(auctionReportRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(alreadyReportAuctionException.getMessage()))
               );
    }

    @Test
    void 경매_아이디가_없는_경우_신고시_400을_반환한다() throws Exception {
        // given
        final CreateAuctionReportRequest auctionReportRequest = new CreateAuctionReportRequest(null, "신고합니다");

        given(auctionReportService.create(any(LoginUserDto.class), any(CreateAuctionReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/auctions").header("Authorization", 1L)
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(auctionReportRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("경매 아이디가 입력되지 않았습니다."))
               );
    }

    @Test
    void 경매_아이디가_음수인_경우_신고시_400을_반환한다() throws Exception {
        // given
        final Long invalidId = -999L;
        final CreateAuctionReportRequest auctionReportRequest = new CreateAuctionReportRequest(invalidId, "신고합니다");

        given(auctionReportService.create(any(LoginUserDto.class), any(CreateAuctionReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/auctions").header("Authorization", 1L)
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(auctionReportRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("경매 아이디는 양수여야 합니다."))
               );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 신고_내용_없이_신고시_400을_반환한다(final String description) throws Exception {
        // given
        final CreateAuctionReportRequest auctionReportRequest = new CreateAuctionReportRequest(1L, description);

        given(auctionReportService.create(any(LoginUserDto.class), any(CreateAuctionReportDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/reports/auctions").header("Authorization", 1L)
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(auctionReportRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("신고 내용이 입력되지 않았습니다."))
               );
    }
}
