package com.ddang.ddang.auction.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionForListSearchByTitleAndSortByReliabilityFixture extends QuerydslAuctionRepositoryImplForListFixture {

    protected Sort 신뢰도순_정렬 = Sort.by(Order.asc("reliability"));
    protected ReadAuctionSearchCondition 검색어_맥북 = new ReadAuctionSearchCondition("맥북");

    protected Auction 첫번째_페이지_인덱스_0_신뢰도_5_0_id_4;
    protected Auction 첫번째_페이지_인덱스_1_신뢰도_4_7_id_7;
    protected Auction 첫번째_페이지_인덱스_2_신뢰도_4_7_id_1;
    protected Auction 두번째_페이지_인덱스_0_신뢰도_4_7_id_16;
    protected Auction 두번째_페이지_인덱스_1_신뢰도_3_5_id_14;
    protected Auction 두번째_페이지_인덱스_2_신뢰도_3_5_id_12;
    protected Auction 세번째_페이지_인덱스_0_신뢰도_3_5_id_10;
    protected Auction 세번째_페이지_인덱스_1_신뢰도_3_5_id_8;
    protected Auction 세번째_페이지_인덱스_2_신뢰도_2_1_id_15;
    protected Auction 네번째_페이지_인덱스_0_신뢰도_2_1_id_13;
    protected Auction 네번째_페이지_인덱스_1_신뢰도_2_1_id_11;
    protected Auction 네번째_페이지_인덱스_2_신뢰도_2_1_id_9;
    protected Auction 다섯번째_페이지_인덱스_0_신뢰도_2_1_id_3;
    protected Auction 다섯번째_페이지_인덱스_1_신뢰도_2_1_id_2;

    @BeforeEach
    void fixtureSetUp() {
        첫번째_페이지_인덱스_0_신뢰도_5_0_id_4 = 경매4;
        첫번째_페이지_인덱스_1_신뢰도_4_7_id_7 = 경매7;
        첫번째_페이지_인덱스_2_신뢰도_4_7_id_1 = 경매1;
        두번째_페이지_인덱스_0_신뢰도_4_7_id_16 = 경매16;
        세번째_페이지_인덱스_2_신뢰도_2_1_id_15 = 경매15;
        두번째_페이지_인덱스_1_신뢰도_3_5_id_14 = 경매14;
        네번째_페이지_인덱스_0_신뢰도_2_1_id_13 = 경매13;
        두번째_페이지_인덱스_2_신뢰도_3_5_id_12 = 경매12;
        네번째_페이지_인덱스_1_신뢰도_2_1_id_11 = 경매11;
        세번째_페이지_인덱스_0_신뢰도_3_5_id_10 = 경매10;
        네번째_페이지_인덱스_2_신뢰도_2_1_id_9 = 경매9;
        세번째_페이지_인덱스_1_신뢰도_3_5_id_8 = 경매8;
        다섯번째_페이지_인덱스_1_신뢰도_2_1_id_2 = 경매2;
        다섯번째_페이지_인덱스_0_신뢰도_2_1_id_3 = 경매3;
    }
}
