package com.ddang.ddang.auction.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionForListSortByAuctioneerCountFixture extends QuerydslAuctionRepositoryImplForListFixture {

    protected Sort 참여_인원순_정렬 = Sort.by(Order.asc("auctioneerCount"));
    protected ReadAuctionSearchCondition 검색어_없음 = new ReadAuctionSearchCondition(null);

    protected Auction 첫번째_페이지_인덱스_0_참여자_7_2일_후_마감_id_4;
    protected Auction 첫번째_페이지_인덱스_1_참여자_6_4일_후_마감_id_15;
    protected Auction 첫번째_페이지_인덱스_2_참여자_6_4일_후_마감_id_14;
    protected Auction 두번째_페이지_인덱스_0_참여자_6_4일_후_마감_id_12;
    protected Auction 두번째_페이지_인덱스_1_참여자_6_4일_후_마감_id_11;
    protected Auction 두번째_페이지_인덱스_2_참여자_6_4일_후_마감_id_10;
    protected Auction 세번째_페이지_인덱스_0_참여자_4_1일_후_마감_id_5;
    protected Auction 세번째_페이지_인덱스_1_참여자_4_3일_후_마감_id_3;
    protected Auction 세번째_페이지_인덱스_2_참여자_1_4일_후_마감_id_2;
    protected Auction 네번째_페이지_인덱스_0_참여자_8_2일_전_마감_id_6;
    protected Auction 네번째_페이지_인덱스_1_참여자_6_4일_전_마감_id_16;
    protected Auction 네번째_페이지_인덱스_2_참여자_6_4일_전_마감_id_13;
    protected Auction 다섯번째_페이지_인덱스_0_참여자_6_4일_전_마감_id_9;
    protected Auction 다섯번째_페이지_인덱스_1_참여자_6_4일_전_마감_id_8;
    protected Auction 다섯번째_페이지_인덱스_2_참여자_3_4일_전_마감_id_7;
    protected Auction 여섯번째_페이지_인덱스_0_참여자_2_5일_전_마감_id_1;

    @BeforeEach
    void fixtureSetUp() {
        첫번째_페이지_인덱스_0_참여자_7_2일_후_마감_id_4 = 경매4;
        첫번째_페이지_인덱스_1_참여자_6_4일_후_마감_id_15 = 경매15;
        첫번째_페이지_인덱스_2_참여자_6_4일_후_마감_id_14 = 경매14;
        두번째_페이지_인덱스_0_참여자_6_4일_후_마감_id_12 = 경매12;
        두번째_페이지_인덱스_1_참여자_6_4일_후_마감_id_11 = 경매11;
        두번째_페이지_인덱스_2_참여자_6_4일_후_마감_id_10 = 경매10;
        세번째_페이지_인덱스_0_참여자_4_1일_후_마감_id_5 = 경매5;
        세번째_페이지_인덱스_1_참여자_4_3일_후_마감_id_3 = 경매3;
        세번째_페이지_인덱스_2_참여자_1_4일_후_마감_id_2 = 경매2;
        네번째_페이지_인덱스_0_참여자_8_2일_전_마감_id_6 = 경매6;
        네번째_페이지_인덱스_1_참여자_6_4일_전_마감_id_16 = 경매16;
        네번째_페이지_인덱스_2_참여자_6_4일_전_마감_id_13 = 경매13;
        다섯번째_페이지_인덱스_0_참여자_6_4일_전_마감_id_9 = 경매9;
        다섯번째_페이지_인덱스_1_참여자_6_4일_전_마감_id_8 = 경매8;
        다섯번째_페이지_인덱스_2_참여자_3_4일_전_마감_id_7 = 경매7;
        여섯번째_페이지_인덱스_0_참여자_2_5일_전_마감_id_1 = 경매1;
    }
}
