package com.ddang.ddang.auction.presentation.fixture;

import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;
import com.ddang.ddang.auction.application.dto.ReadRegionDto;
import com.ddang.ddang.auction.application.dto.ReadRegionsDto;
import com.ddang.ddang.auction.domain.AuctionStatus;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionControllerFixture extends CommonControllerSliceTest {

    protected Long 존재하지_않는_경매_id = -999L;
    protected Long 유효한_경매_id = 1L;
    protected String 경매_이미지_상대_주소 = "/auctions/images";
    protected String 프로필_이미지_상대_주소 = "/users/images";
    protected String 경매_생성_시_경매_상태 = "UNBIDDEN";
    protected int 경매_생성_시_경매_참여자_수 = 0;
    protected String 유효한_액세스_토큰 = "Bearer accessToken";
    protected MockMultipartFile 유효한_경매_이미지_파일;
    protected MockMultipartFile 비어_있는_경매_이미지_파일;
    protected MockMultipartFile 유효하지_않은_확장자_경매_이미지_파일;
    protected MockMultipartFile 유효한_경매_등록_request_multipartFile;
    protected MockMultipartFile 유효하지_않은_카테고리_경매_등록_request_multipartFile;
    protected MockMultipartFile 비어있는_경매_이미지_경매_등록_request_multipartFile;
    protected MockMultipartFile 유효하지_않은_지역_경매_등록_request_multipartFile;
    protected PrivateClaims 유효한_사용자_id_클레임 = new PrivateClaims(1L);
    protected PrivateClaims 유효하지_않은_사용자_id_클레임 = new PrivateClaims(-999L);
    protected CreateInfoAuctionDto 경매_등록_결과_dto = new CreateInfoAuctionDto(
            1L,
            "제목",
            1L,
            1_000
    );
    protected ReadChatRoomDto 쪽지방_dto = new ReadChatRoomDto(1L, true);
    protected ReadAuctionDto 경매_조회_dto;
    protected ReadAuctionDto 첫번째_경매_조회_dto;
    protected ReadAuctionDto 두번째_경매_조회_dto;
    protected ReadAuctionsDto 경매_목록_조회_dto;


    private CreateAuctionRequest 유효한_경매_등록_request = new CreateAuctionRequest(
            "제목",
            "내용",
            1_000,
            1_000,
            LocalDateTime.now().plusDays(3L),
            2L,
            List.of(3L)
    );
    private CreateAuctionRequest 유효하지_않은_카테고리_경매_등록_request = new CreateAuctionRequest(
            "제목",
            "내용",
            1_000,
            1_000,
            LocalDateTime.now().plusDays(3L),
            -999L,
            List.of(3L)
    );
    private CreateAuctionRequest 비어있는_경매_이미지_경매_등록_request = new CreateAuctionRequest(
            "제목",
            "내용",
            1_000,
            1_000,
            LocalDateTime.now().plusDays(3L),
            -999L,
            List.of(3L)
    );
    private CreateAuctionRequest 유효하지_않은_지역_경매_등록_request = new CreateAuctionRequest(
            "제목",
            "내용",
            1_000,
            1_000,
            LocalDateTime.now().plusDays(3L),
            -999L,
            List.of(3L)
    );

    @BeforeEach
    void fixtureSetUp() throws JsonProcessingException {
        유효한_경매_이미지_파일 = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        비어_있는_경매_이미지_파일 = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[0]
        );
        유효하지_않은_확장자_경매_이미지_파일 = new MockMultipartFile(
                "images",
                "image.exe",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        유효한_경매_등록_request_multipartFile = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(유효한_경매_등록_request)
        );
        유효하지_않은_카테고리_경매_등록_request_multipartFile = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(유효하지_않은_카테고리_경매_등록_request)
        );
        비어있는_경매_이미지_경매_등록_request_multipartFile = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(비어있는_경매_이미지_경매_등록_request)
        );
        유효하지_않은_지역_경매_등록_request_multipartFile = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(유효하지_않은_지역_경매_등록_request)
        );

        final ReadRegionsDto 서울특별시_강서구_역삼동 = new ReadRegionsDto(
                new ReadRegionDto(1L, "서울특별시"),
                new ReadRegionDto(2L, "강서구"),
                new ReadRegionDto(3L, "역삼동")
        );

        경매_조회_dto = new ReadAuctionDto(
                1L,
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(서울특별시_강서구_역삼동),
                List.of(1L),
                2,
                "main",
                "sub",
                1L,
                1L,
                "판매자",
                3.5d,
                false,
                AuctionStatus.UNBIDDEN,
                null
        );

        첫번째_경매_조회_dto = new ReadAuctionDto(
                1L,
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(서울특별시_강서구_역삼동),
                List.of(1L),
                2,
                "main",
                "sub",
                1L,
                1L,
                "판매자",
                3.5d,
                false,
                AuctionStatus.UNBIDDEN,
                null
        );

        두번째_경매_조회_dto = new ReadAuctionDto(
                2L,
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(서울특별시_강서구_역삼동),
                List.of(1L),
                2,
                "main",
                "sub",
                1L,
                1L,
                "판매자",
                3.5d,
                false,
                AuctionStatus.UNBIDDEN,
                null
        );

        경매_목록_조회_dto = new ReadAuctionsDto(List.of(두번째_경매_조회_dto, 첫번째_경매_조회_dto), true);
    }
}
