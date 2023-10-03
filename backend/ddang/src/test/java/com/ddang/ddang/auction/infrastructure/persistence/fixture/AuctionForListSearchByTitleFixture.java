package com.ddang.ddang.auction.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionForListSearchByTitleFixture extends QuerydslAuctionRepositoryImplForListFixture {

    protected ReadAuctionSearchCondition 검색어_맥북 = new ReadAuctionSearchCondition("맥북");

    protected Auction 첫번째_페이지_인덱스_0_맥북_검색_id_16;
    protected Auction 첫번째_페이지_인덱스_1_맥북_검색_id_15;
    protected Auction 첫번째_페이지_인덱스_2_맥북_검색_id_14;
    protected Auction 두번째_페이지_인덱스_0_맥북_검색_id_13;
    protected Auction 두번째_페이지_인덱스_1_맥북_검색_id_12;
    protected Auction 두번째_페이지_인덱스_2_맥북_검색_id_11;
    protected Auction 세번째_페이지_인덱스_0_맥북_검색_id_10;
    protected Auction 세번째_페이지_인덱스_1_맥북_검색_id_9;
    protected Auction 세번째_페이지_인덱스_2_맥북_검색_id_8;
    protected Auction 네번째_페이지_인덱스_0_맥북_검색_id_7;
    protected Auction 네번째_페이지_인덱스_1_맥북_검색_id_4;
    protected Auction 네번째_페이지_인덱스_2_맥북_검색_id_3;
    protected Auction 다섯번째_페이지_인덱스_0_맥북_검색_id_2;
    protected Auction 다섯번째_페이지_인덱스_1_맥북_검색_id_1;

    protected ReadAuctionSearchCondition 검색어_캐비어 = new ReadAuctionSearchCondition("캐비어");

    @BeforeEach
    void fixtureSetUp() {
        첫번째_페이지_인덱스_0_맥북_검색_id_16 = 경매16;
        첫번째_페이지_인덱스_1_맥북_검색_id_15 = 경매15;
        첫번째_페이지_인덱스_2_맥북_검색_id_14 = 경매14;
        두번째_페이지_인덱스_0_맥북_검색_id_13 = 경매13;
        두번째_페이지_인덱스_1_맥북_검색_id_12 = 경매12;
        두번째_페이지_인덱스_2_맥북_검색_id_11 = 경매11;
        세번째_페이지_인덱스_0_맥북_검색_id_10 = 경매10;
        세번째_페이지_인덱스_1_맥북_검색_id_9 = 경매9;
        세번째_페이지_인덱스_2_맥북_검색_id_8 = 경매8;
        네번째_페이지_인덱스_0_맥북_검색_id_7 = 경매7;
        네번째_페이지_인덱스_1_맥북_검색_id_4 = 경매4;
        네번째_페이지_인덱스_2_맥북_검색_id_3 = 경매3;
        다섯번째_페이지_인덱스_0_맥북_검색_id_2 = 경매2;
        다섯번째_페이지_인덱스_1_맥북_검색_id_1 = 경매1;
    }
}
