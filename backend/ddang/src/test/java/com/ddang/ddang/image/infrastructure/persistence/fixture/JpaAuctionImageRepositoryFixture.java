package com.ddang.ddang.image.infrastructure.persistence.fixture;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class JpaAuctionImageRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    protected String 업로드_이미지_파일명 = "uploadName";
    protected String 저장된_이미지_파일명 = "storeName";
    protected Long 존재하지_않는_경매_이미지_아이디 = -999L;

    protected AuctionImage 경매_이미지;

    @BeforeEach
    void setUp() {
        경매_이미지 = new AuctionImage(업로드_이미지_파일명, 저장된_이미지_파일명);

        auctionImageRepository.save(경매_이미지);

        em.flush();
        em.clear();
    }
}
