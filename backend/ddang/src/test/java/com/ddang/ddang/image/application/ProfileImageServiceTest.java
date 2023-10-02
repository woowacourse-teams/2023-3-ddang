package com.ddang.ddang.image.application;


import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.image.application.fixture.ProfileImageServiceFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProfileImageServiceTest extends ProfileImageServiceFixture {

    @Autowired
    ImageService imageService;

    @Test
    void 지정한_아이디에_해당하는_이미지를_조회한다() throws Exception {
        // when
        final Resource actual = imageService.readProfileImage(프로필_이미지.getId());

        // then
        assertThat(actual.getFilename()).isEqualTo(프로필_이미지_파일명);
    }

    @Test
    void 지정한_아이디에_해당하는_이미지가_없는_경우_null을_반환한다() throws MalformedURLException {
        // when
        final Resource actual = imageService.readProfileImage(존재하지_않는_프로필_이미지_아이디);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void 지정한_아이디에_해당하는_경매_이미지를_조회한다() throws Exception {
        // when
        final Resource actual = imageService.readAuctionImage(경매_이미지.getId());

        // then
        assertThat(actual.getFilename()).isEqualTo(경매_이미지_파일명);
    }

    @Test
    void 지정한_아이디에_해당하는_경매_이미지가_없는_경우_null을_반환한다() throws MalformedURLException {
        // when
        final Resource actual = imageService.readAuctionImage(존재하지_않는_경매_이미지_아이디);

        // then
        assertThat(actual).isNull();
    }
}
