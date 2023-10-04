package com.ddang.ddang.questionandanswer.domain.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.questionandanswer.domain.Answer;
import com.ddang.ddang.questionandanswer.domain.Question;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class QuestionFixture {

    private ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
    protected User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(프로필_이미지)
                             .reliability(4.7d)
                             .oauthId("12345")
                             .build();
    private Auction 경매 = Auction.builder()
                                .seller(판매자)
                                .title("경매 상품")
                                .description("이것은 경매 상품입니다.")
                                .bidUnit(new BidUnit(1_000))
                                .startPrice(new Price(1_000))
                                .closingTime(LocalDateTime.now())
                                .build();
    private User 질문_작성자 = User.builder()
                              .name("질문 작성자")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
    private String 질문_내용 = "궁금한 점이 있어요.";
    protected User 판매자가_아닌_사용자 = 질문_작성자;

    protected Question 질문 = new Question(경매, 질문_작성자, 질문_내용);
    protected Answer 답변 = new Answer("답변드립니다.");

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(판매자, "id", 1L);
        ReflectionTestUtils.setField(질문_작성자, "id", 2L);
        ReflectionTestUtils.setField(질문, "id", 1L);
        ReflectionTestUtils.setField(답변, "id", 1L);
    }
}
