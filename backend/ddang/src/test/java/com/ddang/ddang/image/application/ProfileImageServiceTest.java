package com.ddang.ddang.image.application;


import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProfileImageServiceTest {

    @Autowired
    ImageService imageService;

    @Autowired
    JpaProfileImageRepository imageRepository;

    @Autowired
    JpaAuctionImageRepository auctionImageRepository;

    @Test
    void 지정한_아이디에_해당하는_이미지를_조회한다() throws Exception {
        // given
        final ProfileImage profileImage = new ProfileImage("image.png", "image.png");

        imageRepository.save(profileImage);

        // when
        final Resource actual = imageService.readProfileImage(profileImage.getId());

        // then
        assertThat(actual.getFilename()).isEqualTo("image.png");
    }

    @Test
    void 지정한_아이디에_해당하는_이미지가_없는_경우_null을_반환한다() throws MalformedURLException {
        // given
        final Long invalidImageId = -999L;

        // when
        final Resource actual = imageService.readProfileImage(invalidImageId);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void 지정한_아이디에_해당하는_경매_이미지를_조회한다() throws Exception {
        // given
        final AuctionImage auctionImage = new AuctionImage("image.png", "image.png");

        auctionImageRepository.save(auctionImage);

        // when
        final Resource actual = imageService.readAuctionImage(auctionImage.getId());

        // then
        assertThat(actual.getFilename()).isEqualTo("image.png");
    }

    @Test
    void 지정한_아이디에_해당하는_경매_이미지가_없는_경우_null을_반환한다() throws MalformedURLException {
        // given
        final Long invalidAuctionImageId = -999L;

        // when
        final Resource actual = imageService.readAuctionImage(invalidAuctionImageId);

        // then
        assertThat(actual).isNull();
    }
}
