package com.ddang.ddang.qna.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.qna.application.dto.CreateQuestionDto;
import com.ddang.ddang.qna.application.dto.ReadAnswerDto;
import com.ddang.ddang.qna.application.dto.ReadQuestionDto;
import com.ddang.ddang.qna.application.dto.ReadUserInQnaDto;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.domain.repository.AnswerRepository;
import com.ddang.ddang.qna.domain.repository.QuestionRepository;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.domain.repository.RegionRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class QuestionServiceFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    protected Long 질문_3개_답변_2개가_존재하는_경매_아이디;
    protected Long 존재하지_않는_경매_아이디 = -999L;
    protected Long 존재하지_않는_질문_아이디 = -999L;
    protected Long 존재하지_않는_사용자_아이디 = -999L;
    protected Question 질문;
    protected User 질문자;
    protected User 질문하지_않은_사용자;
    protected User 두번째_질문을_작성한_사용자;
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

    protected String 이미지_절대_경로 = "/imageUrl";

    @BeforeEach
    void setUp() {
        final Region 서울특별시 = new Region("서울특별시");
        final Region 강남구 = new Region("강남구");
        final Region 역삼동 = new Region("역삼동");

        서울특별시.addSecondRegion(강남구);
        강남구.addThirdRegion(역삼동);

        regionRepository.save(서울특별시);

        final Category 가구_카테고리 = new Category("가구");
        final Category 가구_서브_의자_카테고리 = new Category("의자");

        가구_카테고리.addSubCategory(가구_서브_의자_카테고리);

        categoryRepository.save(가구_카테고리);

        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        final User 판매자 = User.builder()
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
                                  .subCategory(가구_서브_의자_카테고리)
                                  .build();
        final Auction 질문과_답변이_존재하는_경매 = Auction.builder()
                                               .seller(판매자)
                                               .title("경매 상품 1")
                                               .description("이것은 경매 상품 1 입니다.")
                                               .bidUnit(new BidUnit(1_000))
                                               .startPrice(new Price(1_000))
                                               .closingTime(LocalDateTime.now().plusDays(7))
                                               .subCategory(가구_서브_의자_카테고리)
                                               .build();
        final Auction 종료된_경매 = Auction.builder()
                                      .seller(판매자)
                                      .title("경매 상품 1")
                                      .description("이것은 경매 상품 1 입니다.")
                                      .bidUnit(new BidUnit(1_000))
                                      .startPrice(new Price(1_000))
                                      .closingTime(LocalDateTime.now().minusDays(7))
                                      .subCategory(가구_서브_의자_카테고리)
                                      .build();
        final Auction 삭제된_경매 = Auction.builder()
                                      .seller(판매자)
                                      .title("경매 상품 1")
                                      .description("이것은 경매 상품 1 입니다.")
                                      .bidUnit(new BidUnit(1_000))
                                      .startPrice(new Price(1_000))
                                      .closingTime(LocalDateTime.now().plusDays(7))
                                      .subCategory(가구_서브_의자_카테고리)
                                      .build();
        삭제된_경매.delete();
        질문자 = User.builder()
                  .name("질문자")
                  .profileImage(프로필_이미지)
                  .reliability(new Reliability(4.7d))
                  .oauthId("12346")
                  .build();
        질문하지_않은_사용자 = User.builder()
                          .name("사용자")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12346")
                          .build();
        두번째_질문을_작성한_사용자 = User.builder()
                              .name("두번째 질문자")
                              .profileImage(프로필_이미지)
                              .reliability(new Reliability(4.7d))
                              .oauthId("12347")
                              .build();
        질문 = new Question(질문과_답변이_존재하는_경매, 질문자, "질문1");
        final Question 질문2 = new Question(질문과_답변이_존재하는_경매, 두번째_질문을_작성한_사용자, "질문2");
        final Question 질문3 = new Question(질문과_답변이_존재하는_경매, 질문자, "질문3");
        final Answer 답변1 = new Answer("답변1");
        final Answer 답변2 = new Answer("답변2");
        질문.addAnswer(답변1);
        질문2.addAnswer(답변2);

        userRepository.save(판매자);
        userRepository.save(질문자);
        userRepository.save(질문하지_않은_사용자);
        userRepository.save(두번째_질문을_작성한_사용자);

        auctionRepository.save(경매);
        auctionRepository.save(질문과_답변이_존재하는_경매);
        auctionRepository.save(종료된_경매);
        auctionRepository.save(삭제된_경매);
        questionRepository.save(질문);
        questionRepository.save(질문2);
        questionRepository.save(질문3);
        answerRepository.save(답변1);
        answerRepository.save(답변2);

        질문_3개_답변_2개가_존재하는_경매_아이디 = 질문과_답변이_존재하는_경매.getId();

        경매_질문_등록_요청_dto = new CreateQuestionDto(경매.getId(), "궁금한 점이 있습니다.", 질문자.getId());
        존재하지_않는_사용자가_경매_질문_등록_요청_dto = new CreateQuestionDto(경매.getId(), "궁금한 점이 있습니다.", -999L);
        존재하지_않는_경매_질문_등록_요청_dto = new CreateQuestionDto(-999L, "궁금한 점이 있습니다.", 질문자.getId());
        종료된_경매_질문_등록_요청_dto = new CreateQuestionDto(종료된_경매.getId(), "궁금한 점이 있습니다.", 질문자.getId());
        삭제된_경매_질문_등록_요청_dto = new CreateQuestionDto(삭제된_경매.getId(), "궁금한 점이 있습니다.", 질문자.getId());
        판매자가_본인_경매_질문_등록_요청_dto = new CreateQuestionDto(경매.getId(), "궁금한 점이 있습니다.", 판매자.getId());

        final ReadUserInQnaDto 판매자_정보_dto = ReadUserInQnaDto.from(판매자);
        final ReadUserInQnaDto 질문자_정보_dto = ReadUserInQnaDto.from(질문자);
        final ReadUserInQnaDto 두번째_질문자_정보_dto = ReadUserInQnaDto.from(두번째_질문을_작성한_사용자);
        질문_정보_dto1 = new ReadQuestionDto(질문.getId(), 질문자_정보_dto, 질문.getContent(), 질문.getCreatedTime(), false);
        질문_정보_dto2 = new ReadQuestionDto(질문2.getId(), 두번째_질문자_정보_dto, 질문2.getContent(), 질문2.getCreatedTime(), true);
        질문_정보_dto3 = new ReadQuestionDto(질문3.getId(), 질문자_정보_dto, 질문3.getContent(), 질문3.getCreatedTime(), false);
        답변_정보_dto1 = new ReadAnswerDto(답변1.getId(), 판매자_정보_dto, 답변1.getContent(), 답변1.getCreatedTime());
        답변_정보_dto2 = new ReadAnswerDto(답변2.getId(), 판매자_정보_dto, 답변2.getContent(), 답변2.getCreatedTime());
    }
}
