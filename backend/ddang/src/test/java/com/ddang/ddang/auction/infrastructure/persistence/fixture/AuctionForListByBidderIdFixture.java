package com.ddang.ddang.auction.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionForListByBidderIdFixture extends QuerydslAuctionRepositoryForListFixture {

    protected User 참여한_경매가_7개인_사용자;
    protected Auction 첫번째_페이지_인덱스_0;
    protected Auction 첫번째_페이지_인덱스_1;
    protected Auction 첫번째_페이지_인덱스_2;
    protected Auction 두번째_페이지_인덱스_0;
    protected Auction 두번째_페이지_인덱스_1;
    protected Auction 두번째_페이지_인덱스_2;
    protected Auction 세번째_페이지_인덱스_0;

    protected User 참여한_경매가_없는_사용자 = 판매자_0_3점_2;

    @BeforeEach
    void fixtureSetUp() {
        참여한_경매가_7개인_사용자 = 판매자_4_7점;
        첫번째_페이지_인덱스_0 = 경매16;
        첫번째_페이지_인덱스_1 = 경매1;
        첫번째_페이지_인덱스_2 = 경매7;
        두번째_페이지_인덱스_0 = 경매13;
        두번째_페이지_인덱스_1 = 경매9;
        두번째_페이지_인덱스_2 = 경매8;
        세번째_페이지_인덱스_0 = 경매6;

        참여한_경매가_없는_사용자 = 판매자_0_3점_2;
    }
}
