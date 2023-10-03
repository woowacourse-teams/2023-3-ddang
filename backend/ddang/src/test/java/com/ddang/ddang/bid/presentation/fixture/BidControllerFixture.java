package com.ddang.ddang.bid.presentation.fixture;

import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.bid.presentation.dto.request.CreateBidRequest;
import com.ddang.ddang.configuration.CommonControllerSliceTest;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class BidControllerFixture extends CommonControllerSliceTest {

    protected PrivateClaims 사용자_ID_클레임 = new PrivateClaims(1L);
    protected PrivateClaims 존재하지_않는_사용자_ID_클레임 = new PrivateClaims(-999L);
    protected Long 생성된_입찰_아이디 = 1L;
    protected String 액세스_토큰_값 = "Bearer accessToken";
    protected Long 조회하려는_경매_아이디 = -999L;
    protected Long 존재하지_않는_경매_아이디 = -999L;

    protected CreateBidRequest 입찰_request = new CreateBidRequest(1L, 10_000);
    protected CreateBidRequest 존재하지_않는_경매에_대한_입찰_request = new CreateBidRequest(1L, 10_000);
    protected CreateBidRequest 경매_아이디_없이_입찰_request = new CreateBidRequest(null, 10_000);
    protected static CreateBidRequest 경매_아이디가_양수가_아닌_입찰_request1 = new CreateBidRequest(-1L, 10_000);
    protected static CreateBidRequest 경매_아이디가_양수가_아닌_입찰_request2 = new CreateBidRequest(0L, 10_000);
    protected CreateBidRequest 입찰액_없이_입찰_request = new CreateBidRequest(1L, null);
    protected static CreateBidRequest 입찰액이_양수가_아닌_입찰_request1 = new CreateBidRequest(1L, -1);
    protected static CreateBidRequest 입찰액이_양수가_아닌_입찰_request2 = new CreateBidRequest(1L, 0);

    protected ReadBidDto 입찰_정보_dto1 = new ReadBidDto("사용자1", 1L, false, 10_000, LocalDateTime.now());
    protected ReadBidDto 입찰_정보_dto2 = new ReadBidDto("사용자2", 2L, false, 12_000, LocalDateTime.now());
}
