package com.ddang.ddang.report.presentation;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.application.exception.UserNotFoundException;
import com.ddang.ddang.bid.presentation.resolver.LoginUserArgumentResolver;
import com.ddang.ddang.configuration.RestDocsConfiguration;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.report.application.AuctionReportService;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionInReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;
import com.ddang.ddang.report.application.dto.ReadReporterDto;
import com.ddang.ddang.report.application.exception.AlreadyReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReporterToAuctionException;
import com.ddang.ddang.report.presentation.dto.request.CreateAuctionReportRequest;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void 전체_신고_목록을_조회한다() throws Exception {
        // given
        final ReadAuctionReportDto auctionReportDto1 = new ReadAuctionReportDto(
                1L,
                new ReadReporterDto(1L, "회원1", "이미지1", 5.0),
                LocalDateTime.now(),
                new ReadAuctionInReportDto(1L, "제목", "설명", 100, 1_00, false, LocalDateTime.now().plusDays(2), 2),
                "신고합니다."
        );
        final ReadAuctionReportDto auctionReportDto2 = new ReadAuctionReportDto(
                2L,
                new ReadReporterDto(2L, "회원2", "이미지2", 5.0),
                LocalDateTime.now(),
                new ReadAuctionInReportDto(1L, "제목", "설명", 100, 1_00, false, LocalDateTime.now().plusDays(2), 2),
                "신고합니다."
        );
        final ReadAuctionReportDto auctionReportDto3 = new ReadAuctionReportDto(
                3L,
                new ReadReporterDto(3L, "회원3", "이미지3", 5.0),
                LocalDateTime.now(),
                new ReadAuctionInReportDto(1L, "제목", "설명", 100, 1_00, false, LocalDateTime.now().plusDays(2), 2),
                "신고합니다."
        );

        given(auctionReportService.readAll())
                .willReturn(List.of(auctionReportDto1, auctionReportDto2, auctionReportDto3));

        // when & then
        mockMvc.perform(get("/reports/auctions").contentType(MediaType.APPLICATION_JSON))
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
}
