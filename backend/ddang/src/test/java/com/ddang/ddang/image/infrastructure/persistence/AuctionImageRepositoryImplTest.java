package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.repository.AuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.fixture.AuctionImageRepositoryFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionImageRepositoryImplTest extends AuctionImageRepositoryFixture {

    AuctionImageRepository auctionImageRepository;

    @BeforeEach
    void setUp(@Autowired JpaAuctionImageRepository jpaAuctionImageRepository) {
        auctionImageRepository = new AuctionImageRepositoryImpl(jpaAuctionImageRepository);
    }

    @Test
    void 경매_이미지를_아이디를_통해_조회한다() {
        // when
        final Optional<AuctionImage> actual = auctionImageRepository.findById(경매_이미지.getId());

        // then
        assertThat(actual).contains(경매_이미지);
    }
}
