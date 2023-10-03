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
public class JpaQuestionRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaQuestionRepository questionRepository;

    @Autowired
    JpaAnswerRepository answerRepository;

    protected Auction 경매;
    protected Auction 질문이_3개_답변이_2개인_경매;
    protected User 질문자;
    protected String 질문_내용 = "궁금한 점이 있어요.";
    protected Question 질문1;
    protected Question 질문2;
    protected Question 질문3;
    protected Answer 답변1;
    protected Answer 답변2;

    @BeforeEach
    void setUp() {
        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        final User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(프로필_이미지)
                             .reliability(4.7d)
                             .oauthId("12345")
                             .build();
        경매 = Auction.builder()
                    .seller(판매자)
                    .title("경매 상품")
                    .description("이것은 경매 상품입니다.")
                    .bidUnit(new BidUnit(1_000))
                    .startPrice(new Price(1_000))
                    .closingTime(LocalDateTime.now())
                    .build();
        질문이_3개_답변이_2개인_경매 = Auction.builder()
                                   .seller(판매자)
                                   .title("경매 상품")
                                   .description("이것은 경매 상품입니다.")
                                   .bidUnit(new BidUnit(1_000))
                                   .startPrice(new Price(1_000))
                                   .closingTime(LocalDateTime.now())
                                   .build();
        질문자 = User.builder()
                  .name("질문자")
                  .profileImage(프로필_이미지)
                  .reliability(4.7d)
                  .oauthId("12346")
                  .build();

        질문1 = new Question(질문이_3개_답변이_2개인_경매, 질문자, "질문1");
        질문2 = new Question(질문이_3개_답변이_2개인_경매, 질문자, "질문2");
        질문3 = new Question(질문이_3개_답변이_2개인_경매, 질문자, "질문3");
        답변1 = new Answer("답변1");
        답변2 = new Answer("답변2");
        질문1.addAnswer(답변1);
        질문2.addAnswer(답변2);

        userRepository.saveAll(List.of(판매자, 질문자));
        auctionRepository.saveAll(List.of(경매, 질문이_3개_답변이_2개인_경매));
        questionRepository.saveAll(List.of(질문1, 질문2, 질문3));
        answerRepository.saveAll(List.of(답변1, 답변2));

        em.flush();
        em.clear();
    }
}
