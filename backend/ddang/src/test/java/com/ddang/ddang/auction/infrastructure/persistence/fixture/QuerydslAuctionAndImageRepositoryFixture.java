package com.ddang.ddang.auction.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.AuctionRepositoryImpl;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class QuerydslAuctionAndImageRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private JpaAuctionRepository jpaAuctionRepository;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    @Autowired
    private JpaUserRepository userRepository;

    private User 사용자;
    protected Auction 경매;
    protected AuctionImage 경매_이미지;

    @BeforeEach
    void setUp() {
        경매 = Auction.builder()
                    .title("경매 상품 1")
                    .description("이것은 경매 상품 1 입니다.")
                    .bidUnit(new BidUnit(1_000))
                    .startPrice(new Price(1_000))
                    .closingTime(LocalDateTime.now())
                    .build();
        경매_이미지 = new AuctionImage("upload.png", "store.png");
        auctionImageRepository.save(경매_이미지);
        경매.addAuctionImages(List.of(경매_이미지));

        사용자 = User.builder()
                  .name("사용자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(4.7d))
                  .oauthId("12345")
                  .build();

        final AuctionRepository auctionRepository = new AuctionRepositoryImpl(
                jpaAuctionRepository,
                new QuerydslAuctionRepository(jpaQueryFactory)
        );
        auctionRepository.save(경매);
        userRepository.save(사용자);

        em.flush();
        em.clear();
    }
}
