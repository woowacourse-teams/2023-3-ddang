package com.ddang.ddang.report.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.AuctionRepositoryImpl;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.infrastructure.JpaQuestionRepository;
import com.ddang.ddang.report.domain.QuestionReport;
import com.ddang.ddang.report.domain.repository.QuestionReportRepository;
import com.ddang.ddang.report.infrastructure.persistence.JpaQuestionReportRepository;
import com.ddang.ddang.report.infrastructure.persistence.QuestionReportRepositoryImpl;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.ddang.ddang.user.infrastructure.persistence.UserRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class JpaQuestionReportRepositoryImplFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaQuestionRepository questionRepository;

    private UserRepository userRepository;

    private AuctionRepository auctionRepository;

    private QuestionReportRepository questionReportRepository;

    protected User 신고자;
    protected Question 질문;
    protected Question 이미_신고한_질문;
    protected String 신고_내용 = "신고합니다.";
    protected QuestionReport 질문_신고1;
    protected QuestionReport 질문_신고2;
    protected QuestionReport 질문_신고3;
    protected QuestionReport 질문_신고4;

    @BeforeEach
    void setUpFixture(
            @Autowired final JPAQueryFactory jpaQueryFactory,
            @Autowired final JpaUserRepository jpaUserRepository,
            @Autowired final JpaAuctionRepository jpaAuctionRepository,
            @Autowired final JpaQuestionReportRepository jpaQuestionReportRepository
    ) {
        userRepository = new UserRepositoryImpl(jpaUserRepository);
        auctionRepository = new AuctionRepositoryImpl(jpaAuctionRepository, new QuerydslAuctionRepository(jpaQueryFactory));
        questionReportRepository = new QuestionReportRepositoryImpl(jpaQuestionReportRepository);

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
        신고자 = User.builder()
                  .name("신고자")
                  .profileImage(프로필_이미지)
                  .reliability(new Reliability(4.7d))
                  .oauthId("12347")
                  .build();
        final User 신고자2 = User.builder()
                              .name("신고자2")
                              .profileImage(프로필_이미지)
                              .reliability(new Reliability(4.7d))
                              .oauthId("12348")
                              .build();
        final User 신고자3 = User.builder()
                              .name("신고자3")
                              .profileImage(프로필_이미지)
                              .reliability(new Reliability(4.7d))
                              .oauthId("12349")
                              .build();
        final User 신고자4 = User.builder()
                              .name("신고자4")
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

        질문 = new Question(경매, 질문자, "질문드립니다.");
        이미_신고한_질문 = new Question(경매, 질문자, "질문드립니다.");

        질문_신고1 = new QuestionReport(신고자, 이미_신고한_질문, "신고합니다");
        질문_신고2 = new QuestionReport(신고자2, 이미_신고한_질문, "신고합니다");
        질문_신고3 = new QuestionReport(신고자3, 이미_신고한_질문, "신고합니다");
        질문_신고4 = new QuestionReport(신고자4, 이미_신고한_질문, "신고합니다");

        userRepository.save(판매자);
        userRepository.save(질문자);
        userRepository.save(신고자);
        userRepository.save(신고자2);
        userRepository.save(신고자3);
        userRepository.save(신고자4);

        categoryRepository.saveAll(List.of(전자기기_카테고리, 전자기기_서브_노트북_카테고리));
        auctionRepository.save(경매);

        questionRepository.saveAll(List.of(질문, 이미_신고한_질문));
        questionReportRepository.save(질문_신고1);
        questionReportRepository.save(질문_신고2);
        questionReportRepository.save(질문_신고3);
        questionReportRepository.save(질문_신고4);

        em.flush();
        em.clear();
    }
}
