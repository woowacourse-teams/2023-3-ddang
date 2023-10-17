package com.ddang.ddang.qna.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.qna.application.dto.CreateAnswerDto;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.domain.repository.AnswerRepository;
import com.ddang.ddang.qna.domain.repository.QuestionRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class AnswerServiceFixture {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    protected Long 존재하지_않는_답변_아이디 = -999L;
    protected Long 존재하지_않는_사용자_아이디 = -999L;
    protected String 이미지_절대_경로 = "/imageUrl";

    protected Question 답변한_질문;
    protected Answer 답변;
    protected User 판매자;
    protected User 판매자가_아닌_사용자;

    protected CreateAnswerDto 답변_등록_요청_dto;
    protected CreateAnswerDto 존재하지_않는_사용자의_답변_등록_요청_dto;
    protected CreateAnswerDto 존재하지_않는_질문에_답변_등록_요청_dto;
    protected CreateAnswerDto 판매자가_아닌_사용자가_질문에_답변_등록_요청_dto;
    protected CreateAnswerDto 이미_답변한_질문에_답변_등록_요청_dto;
    protected CreateAnswerDto 답변이_삭제된_질문에_답변_등록_요청_dto;

    @BeforeEach
    void setUp() {
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
                                  .closingTime(LocalDateTime.now().plusDays(7))
                                  .build();
        final User 질문자 = User.builder()
                             .name("질문자")
                             .profileImage(프로필_이미지)
                             .reliability(new Reliability(4.7d))
                             .oauthId("12346")
                             .build();
        판매자가_아닌_사용자 = User.builder()
                          .name("판매자가 아닌 사용자")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12347")
                          .build();
        final Question 질문 = new Question(경매, 질문자, "궁금한 점이 있습니다.");
        final Question 삭제된_답변이_있는_질문 = new Question(경매, 질문자, "궁금한 점이 있습니다.");
        답변한_질문 = new Question(경매, 질문자, "궁금한 점이 있습니다.");
        답변 = new Answer(판매자, "답변드립니다.");
        답변한_질문.addAnswer(답변);

        final Answer 삭제된_답변 = new Answer(판매자, "삭제할 답변입니다");

        userRepository.save(판매자);
        userRepository.save(질문자);
        userRepository.save(판매자가_아닌_사용자);

        auctionRepository.save(경매);

        questionRepository.save(질문);
        questionRepository.save(답변한_질문);
        questionRepository.save(삭제된_답변이_있는_질문);

        answerRepository.save(답변);
        answerRepository.save(삭제된_답변);

        삭제된_답변.delete();

        답변_등록_요청_dto = new CreateAnswerDto(질문.getId(), "답변 드립니다.", 판매자.getId());
        존재하지_않는_사용자의_답변_등록_요청_dto = new CreateAnswerDto(질문.getId(), "답변 드립니다.", -999L);
        존재하지_않는_질문에_답변_등록_요청_dto = new CreateAnswerDto(-999L, "답변 드립니다.", 판매자.getId());
        판매자가_아닌_사용자가_질문에_답변_등록_요청_dto = new CreateAnswerDto(질문.getId(), "답변 드립니다.", 질문자.getId());
        이미_답변한_질문에_답변_등록_요청_dto = new CreateAnswerDto(답변한_질문.getId(), "답변 드립니다.", 판매자.getId());
        답변이_삭제된_질문에_답변_등록_요청_dto = new CreateAnswerDto(삭제된_답변이_있는_질문.getId(), "답변드립니다", 판매자.getId());
    }
}
