package com.ddang.ddang.qna.infrastructure.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.AuctionRepositoryImpl;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.domain.repository.AnswerRepository;
import com.ddang.ddang.qna.domain.repository.QuestionRepository;
import com.ddang.ddang.qna.infrastructure.persistence.AnswerRepositoryImpl;
import com.ddang.ddang.qna.infrastructure.persistence.JpaAnswerRepository;
import com.ddang.ddang.qna.infrastructure.persistence.JpaQuestionRepository;
import com.ddang.ddang.qna.infrastructure.persistence.QuestionRepositoryImpl;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.ddang.ddang.user.infrastructure.persistence.UserRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class AnswerRepositoryImplFixture {

    private UserRepository userRepository;

    private AuctionRepository auctionRepository;

    private QuestionRepository questionRepository;

    private AnswerRepository answerRepository;

    protected User 판매자;
    protected Question 질문;
    protected Question 답변이_존재하는_질문;
    protected Question 답변이_존재하지_않는_질문;
    protected String 답변_내용 = "답변드립니다.";
    protected Answer 답변;
    protected Answer 삭제된_답변;

    @BeforeEach
    void setUpFixture(
            @Autowired final JPAQueryFactory jpaQueryFactory,
            @Autowired final JpaUserRepository jpaUserRepository,
            @Autowired final JpaAuctionRepository jpaAuctionRepository,
            @Autowired final JpaQuestionRepository jpaQuestionRepository,
            @Autowired final JpaAnswerRepository jpaAnswerRepository
    ) {
        userRepository = new UserRepositoryImpl(jpaUserRepository);
        auctionRepository = new AuctionRepositoryImpl(jpaAuctionRepository, new QuerydslAuctionRepository(jpaQueryFactory));
        questionRepository = new QuestionRepositoryImpl(jpaQuestionRepository);
        answerRepository = new AnswerRepositoryImpl(jpaAnswerRepository);

        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        판매자 = User.builder()
                  .name("판매자")
                  .profileImage(프로필_이미지)
                  .reliability(new Reliability(4.7d))
                  .oauthId("12345")
                  .build();
        final Auction 경매 = Auction.builder()
                                  .seller(판매자)
                                  .title("경매 상품 1")
                                  .description("이것은 경매 상품 1 입니다.")
                                  .bidUnit(new BidUnit(1_000))
                                  .startPrice(new Price(1_000))
                                  .closingTime(LocalDateTime.now())
                                  .build();
        final User 질문자 = User.builder()
                             .name("질문자")
                             .profileImage(프로필_이미지)
                             .reliability(new Reliability(4.7d))
                             .oauthId("12346")
                             .build();
        질문 = new Question(경매, 질문자, "궁금한 점이 있어요.");
        답변이_존재하는_질문 = new Question(경매, 질문자, "궁금한 점이 있어요.");
        답변이_존재하지_않는_질문 = 질문;
        final Question 답변이_삭제된_질문 = new Question(경매, 질문자, "궁금한 점이 있어요.");

        답변 = new Answer(판매자, "답변드립니다.");
        답변이_존재하는_질문.addAnswer(답변);
        삭제된_답변 = new Answer(판매자, "답변드립니다.");
        답변이_삭제된_질문.addAnswer(삭제된_답변);
        삭제된_답변.delete();

        userRepository.save(판매자);
        userRepository.save(질문자);
        auctionRepository.save(경매);
        questionRepository.save(질문);
        questionRepository.save(답변이_존재하는_질문);
        questionRepository.save(답변이_삭제된_질문);
        answerRepository.save(답변);
        answerRepository.save(삭제된_답변);
    }
}
