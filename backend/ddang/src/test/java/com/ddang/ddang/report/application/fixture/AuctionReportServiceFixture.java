package com.ddang.ddang.report.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaAuctionReportRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionReportServiceFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaProfileImageRepository profileImageRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private JpaAuctionReportRepository auctionReportRepository;

    protected User 이미_신고한_신고자1;
    protected User 이미_신고한_신고자2;
    protected User 이미_신고한_신고자3;
    protected Auction 경매;

    protected CreateAuctionReportDto 새로운_경매_신고_요청_dto;
    protected CreateAuctionReportDto 존재하지_않는_사용자의_경매_신고_요청_dto;
    protected CreateAuctionReportDto 존재하지_않는_경매_신고_요청_dto;
    protected CreateAuctionReportDto 판매자가_본인의_경매_신고_요청_dto;
    protected CreateAuctionReportDto 삭제된_경매_신고_요청_dto;
    protected CreateAuctionReportDto 이미_신고한_사용자가_경매_신고_요청_dto;

    @BeforeEach
    void setUp() {
        final Long 존재하지_않는_사용자_아이디 = -9999L;
        final Long 존재하지_않는_경매_아이디 = -9999L;

        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        final User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(프로필_이미지)
                             .reliability(new Reliability(4.7d))
                             .oauthId("12345")
                             .build();
        final User 새로운_신고자 = User.builder()
                                 .name("새로운_신고자")
                                 .profileImage(프로필_이미지)
                                 .reliability(new Reliability(4.7d))
                                 .oauthId("12346")
                                 .build();
        이미_신고한_신고자1 = User.builder()
                          .name("신고자1")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12347")
                          .build();
        이미_신고한_신고자2 = User.builder()
                          .name("신고자2")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12348")
                          .build();
        이미_신고한_신고자3 = User.builder()
                          .name("신고자3")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12349")
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

        final Auction 삭제된_경매 = Auction.builder()
                        .seller(판매자)
                        .title("삭제된 경매 상품")
                        .description("이것은 삭제된 경매 상품입니다.")
                        .subCategory(전자기기_서브_노트북_카테고리)
                        .bidUnit(new BidUnit(1_000))
                        .startPrice(new Price(1_000))
                        .closingTime(LocalDateTime.now())
                        .build();
        삭제된_경매.addAuctionImages(List.of(경매_이미지));
        삭제된_경매.delete();

        final AuctionReport 경매_신고1 = new AuctionReport(이미_신고한_신고자1, 경매, "신고합니다");
        final AuctionReport 경매_신고2 = new AuctionReport(이미_신고한_신고자2, 경매, "신고합니다");
        final AuctionReport 경매_신고3 = new AuctionReport(이미_신고한_신고자3, 경매, "신고합니다");

        profileImageRepository.save(프로필_이미지);
        userRepository.saveAll(List.of(판매자, 새로운_신고자, 이미_신고한_신고자1, 이미_신고한_신고자2, 이미_신고한_신고자3));

        categoryRepository.saveAll(List.of(전자기기_카테고리, 전자기기_서브_노트북_카테고리));
        auctionImageRepository.save(경매_이미지);
        auctionRepository.save(경매);
        auctionRepository.save(삭제된_경매);


        auctionReportRepository.saveAll(List.of(경매_신고1, 경매_신고2, 경매_신고3));

        새로운_경매_신고_요청_dto = new CreateAuctionReportDto(
                경매.getId(),
                "신고합니다",
                새로운_신고자.getId()
        );
        존재하지_않는_사용자의_경매_신고_요청_dto = new CreateAuctionReportDto(
                경매.getId(),
                "신고합니다",
                존재하지_않는_사용자_아이디
        );
        존재하지_않는_경매_신고_요청_dto = new CreateAuctionReportDto(
                존재하지_않는_경매_아이디,
                "신고합니다",
                새로운_신고자.getId()
        );
        판매자가_본인의_경매_신고_요청_dto = new CreateAuctionReportDto(
                경매.getId(),
                "신고합니다",
                판매자.getId()
        );
        삭제된_경매_신고_요청_dto = new CreateAuctionReportDto(
                삭제된_경매.getId(),
                "신고합니다",
                새로운_신고자.getId()
        );
        이미_신고한_사용자가_경매_신고_요청_dto = new CreateAuctionReportDto(
                경매.getId(),
                "신고합니다",
                이미_신고한_신고자1.getId()
        );
    }
}
