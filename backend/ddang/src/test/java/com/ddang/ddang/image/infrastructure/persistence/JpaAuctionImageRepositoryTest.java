package com.ddang.ddang.image.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.AuctionImage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import(QuerydslConfiguration.class)
class JpaAuctionImageRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaAuctionImageRepository auctionImageRepository;

    @Test
    void 지정한_아이디에_해당하는_경매_이미지를_조회한다() {
        // given
        final AuctionImage auctionImage = new AuctionImage("uploadName", "storeName");

        auctionImageRepository.save(auctionImage);

        em.flush();
        em.clear();

        // when
        final Optional<AuctionImage> actual = auctionImageRepository.findById(auctionImage.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get()).isEqualTo(auctionImage);
        });
    }

    @Test
    void 지정한_아이디에_해당하는_경매_이미지가_없는_경우_빈_Optional을_반환한다() {
        // given
        final Long invalidAuctionImageId = -999L;

        // when
        final Optional<AuctionImage> actual = auctionImageRepository.findById(invalidAuctionImageId);

        // then
        assertThat(actual).isEmpty();
    }
}
