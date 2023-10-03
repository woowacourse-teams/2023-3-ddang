package com.ddang.ddang.user.presentation.fixture;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.application.dto.ReadRegionDto;
import com.ddang.ddang.auction.application.dto.ReadRegionsDto;
import com.ddang.ddang.auction.domain.AuctionStatus;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserAuctionControllerFixture extends CommonControllerSliceTest {

    protected String 액세스_토큰_값 = "Bearer accessToken";
    protected PrivateClaims 사용자_ID_클레임 = new PrivateClaims(1L);
    protected String 페이지_크기 = "10";
    protected String 페이지 = "1";

    final ReadRegionsDto 직거래_지역_정보_dto = new ReadRegionsDto(
            new ReadRegionDto(1L, "서울특별시"),
            new ReadRegionDto(2L, "강서구"),
            new ReadRegionDto(3L, "역삼동")
    );
    protected ReadAuctionDto 경매_정보_dto1 = new ReadAuctionDto(
            1L,
            "경매 상품 1",
            "이것은 경매 상품 1 입니다.",
            1_000,
            1_000,
            null,
            false,
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of(직거래_지역_정보_dto),
            List.of(1L),
            2,
            "main1",
            "sub1",
            1L,
            1L,
            "판매자",
            3.5d,
            false,
            AuctionStatus.UNBIDDEN
    );
    protected ReadAuctionDto 경매_정보_dto2 = new ReadAuctionDto(
            2L,
            "경매 상품 2",
            "이것은 경매 상품 2 입니다.",
            1_000,
            1_000,
            null,
            false,
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of(직거래_지역_정보_dto),
            List.of(1L),
            2,
            "main2",
            "sub2",
            1L,
            1L,
            "판매자",
            3.5d,
            false,
            AuctionStatus.UNBIDDEN
    );
    protected ReadAuctionsDto 사용자의_경매들_정보_dto = new ReadAuctionsDto(List.of(경매_정보_dto2, 경매_정보_dto1), true);
    protected ReadAuctionsDto 사용자가_참여한_경매들_정보_dto = new ReadAuctionsDto(List.of(경매_정보_dto2, 경매_정보_dto1), true);
}
