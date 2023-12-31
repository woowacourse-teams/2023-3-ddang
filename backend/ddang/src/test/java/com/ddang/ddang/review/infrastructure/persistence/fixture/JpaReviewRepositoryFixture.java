package com.ddang.ddang.review.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.Score;
import com.ddang.ddang.review.infrastructure.persistence.JpaReviewRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class JpaReviewRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaReviewRepository reviewRepository;

    protected User 판매자1;
    protected User 판매자2;
    protected User 평가_안한_경매_판매자;
    protected User 구매자;
    protected Auction 판매자1이_평가한_경매;
    protected Auction 판매자2가_평가한_경매;
    protected Auction 평가_안한_경매;
    protected Review 저장하려는_평가;
    protected Review 구매자가_판매자1에게_받은_평가;
    protected Review 구매자가_판매자2에게_받은_평가;

    @BeforeEach
    void setUp() {
        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        categoryRepository.save(전자기기_카테고리);

        final ProfileImage 평가_안한_판매자_프로필_이미지 = new ProfileImage("no_review_seller_profile.png", "no_review_seller_profile.png");
        final ProfileImage 판매자1_프로필_이미지 = new ProfileImage("seller1_profile.png", "seller1_profile.png");
        final ProfileImage 판매자2_프로필_이미지 = new ProfileImage("seller2_profile.png", "seller2_profile.png");
        final ProfileImage 구매자_프로필_이미지 = new ProfileImage("buyer_profile.png", "buyer_profile.png");

        판매자1 = User.builder()
                    .name("판매자1")
                    .profileImage(판매자1_프로필_이미지)
                    .reliability(new Reliability(4.7d))
                    .oauthId("12345")
                    .build();
        판매자2 = User.builder()
                    .name("판매자2")
                    .profileImage(판매자2_프로필_이미지)
                    .reliability(new Reliability(4.7d))
                    .oauthId("12346")
                    .build();
        평가_안한_경매_판매자 = User.builder()
                               .name("평가 안한 판매자")
                               .profileImage(평가_안한_판매자_프로필_이미지)
                               .reliability(new Reliability(4.7d))
                               .oauthId("12346")
                               .build();
        구매자 = User.builder()
                   .name("구매자")
                   .profileImage(구매자_프로필_이미지)
                   .reliability(new Reliability(4.7d))
                   .oauthId("12347")
                   .build();
        userRepository.saveAll(List.of(판매자1, 판매자2, 평가_안한_경매_판매자, 구매자));

        final AuctionImage 경매1_대표_이미지 = new AuctionImage("경매1_대표_이미지.png", "경매1_대표_이미지.png");
        final AuctionImage 경매2_대표_이미지 = new AuctionImage("경매2_대표_이미지.png", "경매2_대표_이미지.png");
        final AuctionImage 평가_안한_경매_대표_이미지 = new AuctionImage("평가_안한_경매_대표_이미지.png", "평가_안한_경매_대표_이미지.png");

        판매자1이_평가한_경매 = Auction.builder()
                                  .seller(판매자1)
                                  .title("맥북")
                                  .description("맥북 팔아요")
                                  .subCategory(전자기기_서브_노트북_카테고리)
                                  .startPrice(new Price(10_000))
                                  .bidUnit(new BidUnit(1_000))
                                  .closingTime(LocalDateTime.now())
                                  .build();
        판매자2가_평가한_경매 = Auction.builder()
                                  .seller(판매자2)
                                  .title("맥북")
                                  .description("맥북 팔아요")
                                  .subCategory(전자기기_서브_노트북_카테고리)
                                  .startPrice(new Price(10_000))
                                  .bidUnit(new BidUnit(1_000))
                                  .closingTime(LocalDateTime.now())
                                  .build();
        평가_안한_경매 = Auction.builder()
                             .seller(판매자2)
                             .title("맥북")
                             .description("맥북 팔아요")
                             .subCategory(전자기기_서브_노트북_카테고리)
                             .startPrice(new Price(10_000))
                             .bidUnit(new BidUnit(1_000))
                             .closingTime(LocalDateTime.now())
                             .build();

        판매자1이_평가한_경매.addAuctionImages(List.of(경매1_대표_이미지));
        판매자2가_평가한_경매.addAuctionImages(List.of(경매2_대표_이미지));
        평가_안한_경매.addAuctionImages(List.of(평가_안한_경매_대표_이미지));
        auctionRepository.saveAll(List.of(판매자1이_평가한_경매, 판매자2가_평가한_경매, 평가_안한_경매));

        저장하려는_평가 = Review.builder()
                            .auction(평가_안한_경매)
                            .writer(판매자1)
                            .target(구매자)
                            .content("친절하다.")
                            .score(new Score(5.0d))
                            .build();
        구매자가_판매자1에게_받은_평가 = Review.builder()
                                        .auction(판매자1이_평가한_경매)
                                        .writer(판매자1)
                                        .target(구매자)
                                        .content("친절하다.")
                                        .score(new Score(5.0d))
                                        .build();
        구매자가_판매자2에게_받은_평가 = Review.builder()
                                        .auction(판매자2가_평가한_경매)
                                        .writer(판매자2)
                                        .target(구매자)
                                        .content("별로다.")
                                        .score(new Score(1.0d))
                                        .build();
        reviewRepository.saveAll(List.of(구매자가_판매자1에게_받은_평가, 구매자가_판매자2에게_받은_평가));

        em.flush();
        em.clear();
    }
}
