package com.ddang.ddang.auction.infrastructure.persistence.fixture.list;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.fixture.QuerydslAuctionRepositoryForListFixture;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionForListByUserIdFixture extends QuerydslAuctionRepositoryForListFixture {

    protected User 등록한_경매가_5개인_사용자;
    protected Auction 첫번째_페이지_인덱스_0;
    protected Auction 첫번째_페이지_인덱스_1;
    protected Auction 첫번째_페이지_인덱스_2;
    protected Auction 두번째_페이지_인덱스_0;
    protected Auction 두번째_페이지_인덱스_1;

    protected User 등록한_경매가_없는_사용자;

    @BeforeEach
    void fixtureSetUp() {
        등록한_경매가_5개인_사용자 = 판매자_3_5점;
        첫번째_페이지_인덱스_0 = 경매16;
        첫번째_페이지_인덱스_1 = 경매14;
        첫번째_페이지_인덱스_2 = 경매12;
        두번째_페이지_인덱스_0 = 경매10;
        두번째_페이지_인덱스_1 = 경매8;

        등록한_경매가_없는_사용자 = 판매자_0_3점_2;
    }
}
