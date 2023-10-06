package com.ddang.ddang.image.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.image.application.exception.ImageNotFoundException;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageServiceTest {

    @Autowired
    ImageService imageService;

    @Autowired
    JpaAuctionImageRepository auctionImageRepository;

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
    void 지정한_아이디에_해당하는_경매_이미지가_없는_경우_예외가_발생한다() {
        // given
        final Long invalidAuctionImageId = -999L;

        // when & then
        assertThatThrownBy(() -> imageService.readAuctionImage(invalidAuctionImageId))
                .isInstanceOf(ImageNotFoundException.class)
                .hasMessage("지정한 이미지를 찾을 수 없습니다.");
    }
}
