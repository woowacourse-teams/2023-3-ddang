package com.ddang.ddang.review.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.infrastructure.persistence.JpaReviewRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ReviewServiceFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaReviewRepository reviewRepository;

    private Category 전자기기_카테고리 = new Category("전자기기");
    private Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
    private ProfileImage 판매자1_프로필_이미지 = new ProfileImage("seller1_profile.png", "seller1_profile.png");
    private ProfileImage 판매자2_프로필_이미지 = new ProfileImage("seller2_profile.png", "seller2_profile.png");
    private ProfileImage 평가_안한_판매자_프로필_이미지 = new ProfileImage("no_review_seller_profile.png", "no_review_seller_profile.png");
    private ProfileImage 구매자_프로필_이미지 = new ProfileImage("buyer_profile.png", "buyer_profile.png");
    private AuctionImage 경매1_대표_이미지 = new AuctionImage("경매1_대표_이미지.png", "경매1_대표_이미지.png");
    private AuctionImage 경매2_대표_이미지 = new AuctionImage("경매2_대표_이미지.png", "경매2_대표_이미지.png");
    private AuctionImage 평가_안한_경매_대표_이미지 = new AuctionImage("평가_안한_경매_대표_이미지.png", "평가_안한_경매_대표_이미지.png");
    protected User 판매자1 = User.builder()
                              .name("판매자1")
                              .profileImage(판매자1_프로필_이미지)
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
    protected User 판매자2 = User.builder()
                              .name("판매자2")
                              .profileImage(판매자2_프로필_이미지)
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
    protected User 평가_안한_경매_판매자 = User.builder()
                                      .name("평가 안한 판매자")
                                      .profileImage(평가_안한_판매자_프로필_이미지)
                                      .reliability(4.7d)
                                      .oauthId("12346")
                                      .build();
    protected User 구매자 = User.builder()
                             .name("구매자")
                             .profileImage(구매자_프로필_이미지)
                             .reliability(4.7d)
                             .oauthId("12347")
                             .build();
    protected User 경매_참여자가_아닌_사용자 = User.builder()
                                        .name("경매 참여자가 아닌 사용자")
                                        .profileImage(new ProfileImage("profile.png", "profile.png"))
                                        .reliability(4.7d)
                                        .oauthId("12347")
                                        .build();
    protected Long 존재하지_않는_사용자 = -999L;
    protected Auction 판매자1이_평가한_경매 = Auction.builder()
                                            .seller(판매자1)
                                            .title("맥북")
                                            .description("맥북 팔아요")
                                            .subCategory(전자기기_서브_노트북_카테고리)
                                            .startPrice(new Price(10_000))
                                            .bidUnit(new BidUnit(1_000))
                                            .closingTime(LocalDateTime.now())
                                            .build();
    protected Auction 판매자2가_평가한_경매 = Auction.builder()
                                            .seller(판매자2)
                                            .title("맥북")
                                            .description("맥북 팔아요")
                                            .subCategory(전자기기_서브_노트북_카테고리)
                                            .startPrice(new Price(10_000))
                                            .bidUnit(new BidUnit(1_000))
                                            .closingTime(LocalDateTime.now())
                                            .build();
    protected Auction 평가_안한_경매 = Auction.builder()
                                        .seller(평가_안한_경매_판매자)
                                        .title("맥북")
                                        .description("맥북 팔아요")
                                        .subCategory(전자기기_서브_노트북_카테고리)
                                        .startPrice(new Price(10_000))
                                        .bidUnit(new BidUnit(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
    protected Long 존재하지_않는_경매_아이디 = -999L;
    protected Review 구매자가_판매자1에게_받은_평가 = Review.builder()
                                               .auction(판매자1이_평가한_경매)
                                               .writer(판매자1)
                                               .target(구매자)
                                               .content("친절하다.")
                                               .score(5.0d)
                                               .build();
    protected Review 구매자가_판매자2에게_받은_평가 = Review.builder()
                                               .auction(판매자2가_평가한_경매)
                                               .writer(판매자2)
                                               .target(구매자)
                                               .content("별로다.")
                                               .score(1.0d)
                                               .build();
    protected List<Review> 구매자가_이전까지_받은_평가_총2개 = List.of(구매자가_판매자1에게_받은_평가, 구매자가_판매자2에게_받은_평가);

    @BeforeEach
    void setUp() {
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        categoryRepository.save(전자기기_카테고리);

        userRepository.saveAll(List.of(판매자1, 판매자2, 평가_안한_경매_판매자, 구매자, 경매_참여자가_아닌_사용자));

        판매자1이_평가한_경매.addAuctionImages(List.of(경매1_대표_이미지));
        판매자2가_평가한_경매.addAuctionImages(List.of(경매2_대표_이미지));
        평가_안한_경매.addAuctionImages(List.of(평가_안한_경매_대표_이미지));
        auctionRepository.saveAll(List.of(판매자1이_평가한_경매, 판매자2가_평가한_경매, 평가_안한_경매));

        reviewRepository.saveAll(List.of(구매자가_판매자1에게_받은_평가, 구매자가_판매자2에게_받은_평가));
    }
}
