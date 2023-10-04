package com.ddang.ddang.questionandanswer.infrastructure.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.questionandanswer.domain.Answer;
import com.ddang.ddang.questionandanswer.domain.Question;
import com.ddang.ddang.questionandanswer.infrastructure.JpaAnswerRepository;
import com.ddang.ddang.questionandanswer.infrastructure.JpaQuestionRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class JpaAnswerRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaQuestionRepository questionRepository;

    @Autowired
    private  JpaAnswerRepository answerRepository;

    protected Question 질문;
    protected Question 답변이_존재하는_질문;
    protected Question 답변이_존재하지_않는_질문;
    protected String 답변_내용 = "답변드립니다.";

    @BeforeEach
    void setUp() {
        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        final User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(프로필_이미지)
                             .reliability(4.7d)
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
                             .reliability(4.7d)
                             .oauthId("12346")
                             .build();
        질문 = new Question(경매, 질문자, "궁금한 점이 있어요.");
        답변이_존재하는_질문 = new Question(경매, 질문자, "궁금한 점이 있어요.");
        답변이_존재하지_않는_질문 = 질문;

        final Answer 답변 = new Answer("답변드립니다.");
        답변이_존재하는_질문.addAnswer(답변);

        userRepository.saveAll(List.of(판매자, 질문자));
        auctionRepository.save(경매);
        questionRepository.saveAll(List.of(질문, 답변이_존재하는_질문));
        answerRepository.save(답변);

        em.flush();
        em.clear();
    }
}
