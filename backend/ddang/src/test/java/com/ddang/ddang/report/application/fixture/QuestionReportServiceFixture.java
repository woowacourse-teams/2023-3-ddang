package com.ddang.ddang.report.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.infrastructure.JpaQuestionRepository;
import com.ddang.ddang.report.application.dto.CreateQuestionReportDto;
import com.ddang.ddang.report.domain.QuestionReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaQuestionReportRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class QuestionReportServiceFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaQuestionRepository questionRepository;

    @Autowired
    private JpaQuestionReportRepository questionReportRepository;

    protected User 이미_신고한_신고자1;
    protected User 이미_신고한_신고자2;
    protected User 이미_신고한_신고자3;
    protected QuestionReport 질문_신고1;
    protected QuestionReport 질문_신고2;
    protected QuestionReport 질문_신고3;

    protected CreateQuestionReportDto 질문_신고_요청_dto;
    protected CreateQuestionReportDto 존재하지_않는_사용자가_질문_신고_요청_dto;
    protected CreateQuestionReportDto 존재하지_않는_질문_신고_요청_dto;
    protected CreateQuestionReportDto 질문자가_본인_질문_신고_요청_dto;
    protected CreateQuestionReportDto 이미_신고한_질문_신고_요청_dto;

    @BeforeEach
    void setUp() {
        final Long 존재하지_않는_질문_아이디 = -999L;
        final Long 존재하지_않는_사용자_아이디 = -999L;

        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        final User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(프로필_이미지)
                             .reliability(new Reliability(4.7d))
                             .oauthId("12345")
                             .build();
        final User 질문자 = User.builder()
                             .name("질문자")
                             .profileImage(프로필_이미지)
                             .reliability(new Reliability(4.7d))
                             .oauthId("12346")
                             .build();
        final User 신고자 = User.builder()
                             .name("신고자")
                             .profileImage(프로필_이미지)
                             .reliability(new Reliability(4.7d))
                             .oauthId("12347")
                             .build();
        이미_신고한_신고자1 = User.builder()
                          .name("이미 신고한 신고자1")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12348")
                          .build();
        이미_신고한_신고자2 = User.builder()
                          .name("이미 신고한 신고자2")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12349")
                          .build();
        이미_신고한_신고자3 = User.builder()
                          .name("이미 신고한 신고자3")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12350")
                          .build();

        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        final AuctionImage 경매_이미지 = new AuctionImage("경매이미지.jpg", "경매이미지.jpg");
        final Auction 경매 = Auction.builder()
                                  .seller(판매자)
                                  .title("경매 상품")
                                  .description("이것은 경매 상품입니다.")
                                  .subCategory(전자기기_서브_노트북_카테고리)
                                  .bidUnit(new BidUnit(1_000))
                                  .startPrice(new Price(1_000))
                                  .closingTime(LocalDateTime.now())
                                  .build();
        경매.addAuctionImages(List.of(경매_이미지));

        final Question 질문 = new Question(경매, 질문자, "질문드립니다.");
        질문_신고1 = new QuestionReport(이미_신고한_신고자1, 질문, "신고합니다.");
        질문_신고2 = new QuestionReport(이미_신고한_신고자2, 질문, "신고합니다.");
        질문_신고3 = new QuestionReport(이미_신고한_신고자3, 질문, "신고합니다.");

        userRepository.saveAll(List.of(판매자, 질문자, 신고자, 이미_신고한_신고자1, 이미_신고한_신고자2, 이미_신고한_신고자3));
        categoryRepository.saveAll(List.of(전자기기_카테고리, 전자기기_서브_노트북_카테고리));
        auctionRepository.save(경매);
        questionRepository.save(질문);
        questionReportRepository.saveAll(List.of(질문_신고1, 질문_신고2, 질문_신고3));

        질문_신고_요청_dto = new CreateQuestionReportDto(질문.getId(), "신고합니다.", 신고자.getId());
        존재하지_않는_질문_신고_요청_dto = new CreateQuestionReportDto(존재하지_않는_질문_아이디, "신고합니다.", 신고자.getId());
        존재하지_않는_사용자가_질문_신고_요청_dto = new CreateQuestionReportDto(질문.getId(), "신고합니다.", 존재하지_않는_사용자_아이디);
        질문자가_본인_질문_신고_요청_dto = new CreateQuestionReportDto(질문.getId(), "신고합니다.", 질문자.getId());
        이미_신고한_질문_신고_요청_dto = new CreateQuestionReportDto(질문.getId(), "신고합니다.", 이미_신고한_신고자1.getId());
    }
}
