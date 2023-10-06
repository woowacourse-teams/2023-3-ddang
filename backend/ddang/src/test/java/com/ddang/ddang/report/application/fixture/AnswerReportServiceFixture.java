package com.ddang.ddang.report.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.infrastructure.JpaAnswerRepository;
import com.ddang.ddang.qna.infrastructure.JpaQuestionRepository;
import com.ddang.ddang.report.application.dto.CreateAnswerReportDto;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class AnswerReportServiceFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaQuestionRepository questionRepository;

    @Autowired
    private JpaAnswerRepository answerRepository;

    protected User 신고자;
    protected Answer 답변;
    protected String 신고_내용 = "신고합니다.";

    protected CreateAnswerReportDto 질문_신고_요청_dto;

    @BeforeEach
    void setUp() {

        final Long 존재하지_않는_질문_아이디 = -999L;
        final Long 존재하지_않는_사용자_아이디 = -999L;

        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        final User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(프로필_이미지)
                             .reliability(4.7d)
                             .oauthId("12345")
                             .build();
        final User 질문자 = User.builder()
                             .name("질문자")
                             .profileImage(프로필_이미지)
                             .reliability(4.7d)
                             .oauthId("12346")
                             .build();
        final User 답변자 = User.builder()
                             .name("답변자")
                             .profileImage(프로필_이미지)
                             .reliability(4.7d)
                             .oauthId("12347")
                             .build();
        신고자 = User.builder()
                  .name("신고자")
                  .profileImage(프로필_이미지)
                  .reliability(4.7d)
                  .oauthId("12348")
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
        답변 = new Answer("답변드립니다.");
        질문.addAnswer(답변);

        userRepository.saveAll(List.of(판매자, 질문자, 답변자, 신고자));
        categoryRepository.saveAll(List.of(전자기기_카테고리, 전자기기_서브_노트북_카테고리));
        auctionRepository.save(경매);
        questionRepository.save(질문);
        answerRepository.save(답변);
    }
}
