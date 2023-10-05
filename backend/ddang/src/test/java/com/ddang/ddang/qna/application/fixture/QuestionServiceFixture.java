package com.ddang.ddang.qna.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.qna.application.dto.CreateQuestionDto;
import com.ddang.ddang.qna.application.dto.ReadAnswerDto;
import com.ddang.ddang.qna.application.dto.ReadQuestionDto;
import com.ddang.ddang.qna.application.dto.ReadUserInQnaDto;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.infrastructure.JpaAnswerRepository;
import com.ddang.ddang.qna.infrastructure.JpaQuestionRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class QuestionServiceFixture {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaQuestionRepository questionRepository;

    @Autowired
    private JpaAnswerRepository answerRepository;

    protected Long 질문_3개_답변_2개가_존재하는_경매_아이디;
    protected Long 존재하지_않는_경매_아이디 = -999L;
    protected CreateQuestionDto 경매_질문_등록_요청_dto;
    protected CreateQuestionDto 존재하지_않는_사용자가_경매_질문_등록_요청_dto;
    protected CreateQuestionDto 존재하지_않는_경매_질문_등록_요청_dto;
    protected CreateQuestionDto 종료된_경매_질문_등록_요청_dto;
    protected CreateQuestionDto 삭제된_경매_질문_등록_요청_dto;
    protected CreateQuestionDto 판매자가_본인_경매_질문_등록_요청_dto;
    protected ReadQuestionDto 질문_정보_dto1;
    protected ReadQuestionDto 질문_정보_dto2;
    protected ReadQuestionDto 질문_정보_dto3;
    protected ReadAnswerDto 답변_정보_dto1;
    protected ReadAnswerDto 답변_정보_dto2;

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
                                  .closingTime(LocalDateTime.now().plusDays(7))
                                  .build();
        final Auction 질문과_답변이_존재하는_경매 = Auction.builder()
                                               .seller(판매자)
                                               .title("경매 상품 1")
                                               .description("이것은 경매 상품 1 입니다.")
                                               .bidUnit(new BidUnit(1_000))
                                               .startPrice(new Price(1_000))
                                               .closingTime(LocalDateTime.now().plusDays(7))
                                               .build();
        final Auction 종료된_경매 = Auction.builder()
                                      .seller(판매자)
                                      .title("경매 상품 1")
                                      .description("이것은 경매 상품 1 입니다.")
                                      .bidUnit(new BidUnit(1_000))
                                      .startPrice(new Price(1_000))
                                      .closingTime(LocalDateTime.now().minusDays(7))
                                      .build();
        final Auction 삭제된_경매 = Auction.builder()
                                      .seller(판매자)
                                      .title("경매 상품 1")
                                      .description("이것은 경매 상품 1 입니다.")
                                      .bidUnit(new BidUnit(1_000))
                                      .startPrice(new Price(1_000))
                                      .closingTime(LocalDateTime.now().plusDays(7))
                                      .build();
        삭제된_경매.delete();
        final User 질문자 = User.builder()
                             .name("질문자")
                             .profileImage(프로필_이미지)
                             .reliability(4.7d)
                             .oauthId("12346")
                             .build();
        final Question 질문1 = new Question(질문과_답변이_존재하는_경매, 질문자, "질문1");
        final Question 질문2 = new Question(질문과_답변이_존재하는_경매, 질문자, "질문2");
        final Question 질문3 = new Question(질문과_답변이_존재하는_경매, 질문자, "질문3");
        final Answer 답변1 = new Answer("답변1");
        final Answer 답변2 = new Answer("답변2");
        질문1.addAnswer(답변1);
        질문2.addAnswer(답변2);

        userRepository.saveAll(List.of(판매자, 질문자));
        auctionRepository.saveAll(List.of(경매, 질문과_답변이_존재하는_경매, 종료된_경매, 삭제된_경매));
        questionRepository.saveAll(List.of(질문1, 질문2, 질문3));
        answerRepository.saveAll(List.of(답변1, 답변2));

        질문_3개_답변_2개가_존재하는_경매_아이디 = 질문과_답변이_존재하는_경매.getId();

        경매_질문_등록_요청_dto = new CreateQuestionDto(경매.getId(), "궁금한 점이 있습니다.", 질문자.getId());
        존재하지_않는_사용자가_경매_질문_등록_요청_dto = new CreateQuestionDto(경매.getId(), "궁금한 점이 있습니다.", -999L);
        존재하지_않는_경매_질문_등록_요청_dto = new CreateQuestionDto(-999L, "궁금한 점이 있습니다.", 질문자.getId());
        종료된_경매_질문_등록_요청_dto = new CreateQuestionDto(종료된_경매.getId(), "궁금한 점이 있습니다.", 질문자.getId());
        삭제된_경매_질문_등록_요청_dto = new CreateQuestionDto(삭제된_경매.getId(), "궁금한 점이 있습니다.", 질문자.getId());
        판매자가_본인_경매_질문_등록_요청_dto = new CreateQuestionDto(경매.getId(), "궁금한 점이 있습니다.", 판매자.getId());

        final ReadUserInQnaDto 판매자_정보_dto = ReadUserInQnaDto.from(판매자);
        final ReadUserInQnaDto 질문자_정보_dto = ReadUserInQnaDto.from(질문자);
        질문_정보_dto1 = new ReadQuestionDto(질문1.getId(), 질문자_정보_dto, 질문1.getContent(), 질문1.getCreatedTime());
        질문_정보_dto2 = new ReadQuestionDto(질문2.getId(), 질문자_정보_dto, 질문2.getContent(), 질문2.getCreatedTime());
        질문_정보_dto3 = new ReadQuestionDto(질문3.getId(), 질문자_정보_dto, 질문3.getContent(), 질문3.getCreatedTime());
        답변_정보_dto1 = new ReadAnswerDto(답변1.getId(), 판매자_정보_dto, 답변1.getContent(), 답변1.getCreatedTime());
        답변_정보_dto2 = new ReadAnswerDto(답변2.getId(), 판매자_정보_dto, 답변2.getContent(), 답변2.getCreatedTime());
    }
}
