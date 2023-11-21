package com.ddang.ddang.image.infrastructure.persistence.fixture;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionImageRepositoryImplFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaAuctionImageRepository jpaAuctionImageRepository;

    protected AuctionImage 경매_이미지;
    protected String 존재하지_않는_경매_이미지_이름 = "invalid-image.png";

    @BeforeEach
    void fixtureSetUp() {
        경매_이미지 = new AuctionImage("경매이미지.png", "경매이미지.png");

        jpaAuctionImageRepository.save(경매_이미지);

        em.flush();
        em.clear();
    }
}
