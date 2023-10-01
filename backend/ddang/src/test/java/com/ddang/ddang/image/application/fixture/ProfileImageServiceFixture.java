package com.ddang.ddang.image.application.fixture;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class ProfileImageServiceFixture {

    @Autowired
    JpaProfileImageRepository profileImageRepository;

    @Autowired
    JpaAuctionImageRepository auctionImageRepository;

    protected Long 존재하지_않는_프로필_이미지_아이디 = -999L;
    protected Long 존재하지_않는_경매_이미지_아이디 = -999L;

    protected ProfileImage 프로필_이미지;
    protected String 프로필_이미지_파읾명;

    protected AuctionImage 경매_이미지;
    protected String 경매_이미지_파읾명;

    @BeforeEach
    void setUp() {
        프로필_이미지 = new ProfileImage("upload.png", "store.png");
        프로필_이미지_파읾명 = 프로필_이미지.getImage().getStoreName();

        경매_이미지 = new AuctionImage("upload.png", "store.png");
        경매_이미지_파읾명 = 경매_이미지.getImage().getStoreName();

        profileImageRepository.save(프로필_이미지);
        auctionImageRepository.save(경매_이미지);
    }
}
