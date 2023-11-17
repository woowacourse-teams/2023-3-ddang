package com.ddang.ddang.image.infrastructure.persistence.fixture;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionImageRepositoryImplFixture {

    @Autowired
    private JpaAuctionImageRepository jpaAuctionImageRepository;

    protected String 존재하는_경매_이미지_이름 = "경매이미지.png";
    protected String 존재하지_않는_경매_이미지_이름 = "invalid.png";

    @BeforeEach
    void fixtureSetUp() {
        final AuctionImage 경매_이미지 = new AuctionImage("경매이미지.png", 존재하는_경매_이미지_이름);

        jpaAuctionImageRepository.save(경매_이미지);
    }
}
