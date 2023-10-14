package com.ddang.ddang.report.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaAuctionReportRepository;
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
public class AuctionReportRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaProfileImageRepository profileImageRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaAuctionReportRepository auctionReportRepository;

    protected Long 존재하지_않는_경매_아이디 = -9999L;
    protected Long 존재하지_않는_사용자_아이디 = -9999L;
    protected User 판매자;
    protected User 신고자1;
    protected Auction 경매;
    protected AuctionReport 경매_신고1;
    protected AuctionReport 경매_신고2;
    protected AuctionReport 경매_신고3;

    @BeforeEach
    void setUp() {
        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        판매자 = User.builder()
                  .name("판매자")
                  .profileImage(프로필_이미지)
                  .reliability(new Reliability(4.7d))
                  .oauthId("12345")
                  .build();
        신고자1 = User.builder()
                   .name("신고자1")
                   .profileImage(프로필_이미지)
                   .reliability(new Reliability(4.7d))
                   .oauthId("12346")
                   .build();
        final User 신고자2 = User.builder()
                              .name("신고자2")
                              .profileImage(프로필_이미지)
                              .reliability(new Reliability(4.7d))
                              .oauthId("12347")
                              .build();
        final User 신고자3 = User.builder()
                              .name("신고자3")
                              .profileImage(프로필_이미지)
                              .reliability(new Reliability(4.7d))
                              .oauthId("12348")
                              .build();

        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        final AuctionImage 경매_이미지 = new AuctionImage("경매이미지.jpg", "경매이미지.jpg");
        경매 = Auction.builder()
                    .seller(판매자)
                    .title("경매 상품")
                    .description("이것은 경매 상품입니다.")
                    .subCategory(전자기기_서브_노트북_카테고리)
                    .bidUnit(new BidUnit(1_000))
                    .startPrice(new Price(1_000))
                    .closingTime(LocalDateTime.now())
                    .build();
        경매.addAuctionImages(List.of(경매_이미지));

        경매_신고1 = new AuctionReport(신고자1, 경매, "신고합니다");
        경매_신고2 = new AuctionReport(신고자2, 경매, "신고합니다");
        경매_신고3 = new AuctionReport(신고자3, 경매, "신고합니다");


        profileImageRepository.save(프로필_이미지);
        userRepository.saveAll(List.of(판매자, 신고자1, 신고자2, 신고자3));

        categoryRepository.saveAll(List.of(전자기기_카테고리, 전자기기_서브_노트북_카테고리));
        auctionImageRepository.save(경매_이미지);
        auctionRepository.save(경매);

        auctionReportRepository.saveAll(List.of(경매_신고1, 경매_신고2, 경매_신고3));

        em.flush();
        em.clear();
    }
}
