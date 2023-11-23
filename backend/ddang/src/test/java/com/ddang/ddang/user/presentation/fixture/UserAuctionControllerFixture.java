package com.ddang.ddang.user.presentation.fixture;

import com.ddang.ddang.auction.application.dto.response.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.response.ReadAuctionsDto;
import com.ddang.ddang.auction.application.dto.response.ReadRegionsDto;
import com.ddang.ddang.auction.domain.AuctionStatus;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
public class UserAuctionControllerFixture extends CommonControllerSliceTest {

    protected String 액세스_토큰_값 = "Bearer accessToken";
    protected PrivateClaims 사용자_ID_클레임 = new PrivateClaims(1L);
    protected String 페이지_크기 = "10";
    protected String 페이지 = "1";

    protected ReadAuctionDto 경매_정보_dto1;
    protected ReadAuctionDto 경매_정보_dto2;
    protected ReadAuctionsDto 사용자의_경매들_정보_dto;
    protected ReadAuctionsDto 사용자가_참여한_경매들_정보_dto;

    @BeforeEach
    void fixtureSetUp() {
        final Region 서울특별시 = new Region("서울특별시");
        ReflectionTestUtils.setField(서울특별시, "id", 1L);

        final Region 강서구 = new Region("강서구");
        ReflectionTestUtils.setField(강서구, "id", 2L);

        final Region 역삼동 = new Region("역삼동");
        ReflectionTestUtils.setField(역삼동, "id", 3L);

        강서구.addThirdRegion(역삼동);
        서울특별시.addSecondRegion(강서구);

        final ReadRegionsDto 직거래_지역_정보_dto = ReadRegionsDto.from(new AuctionRegion(역삼동));

        경매_정보_dto1 = new ReadAuctionDto(
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
                List.of("store-name.png"),
                2,
                "main1",
                "sub1",
                1L,
                "store-name.png",
                "판매자",
                3.5f,
                false,
                AuctionStatus.UNBIDDEN,
                null
        );

        경매_정보_dto2 = new ReadAuctionDto(
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
                List.of("store-name.png"),
                2,
                "main2",
                "sub2",
                1L,
                "store-name.png",
                "판매자",
                3.5f,
                false,
                AuctionStatus.UNBIDDEN,
                null
        );

        사용자의_경매들_정보_dto = new ReadAuctionsDto(List.of(경매_정보_dto2, 경매_정보_dto1), true);
        사용자가_참여한_경매들_정보_dto = new ReadAuctionsDto(List.of(경매_정보_dto2, 경매_정보_dto1), true);
    }
}
