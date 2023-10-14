package com.ddang.ddang.user.application.schedule.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.Reviews;
import com.ddang.ddang.review.domain.Score;
import com.ddang.ddang.review.domain.repository.ReviewRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.ReliabilityUpdateHistory;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.UserReliability;
import com.ddang.ddang.user.infrastructure.persistence.JpaReliabilityUpdateHistoryRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserReliabilityRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ReliabilityUpdateSchedulingServiceFixture {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private JpaUserReliabilityRepository userReliabilityRepository;

    @Autowired
    private JpaReliabilityUpdateHistoryRepository reliabilityUpdateHistoryRepository;

    private double 구매자1_기존_신뢰도_점수 = 3.5d;
    private double 구매자2_기존_신뢰도_점수 = 4.0d;
    private double 구매자3_기존_신뢰도_점수 = 4.5d;
    private double 구매자1_새로운_평가1_점수 = 5.0d;
    private double 구매자2_새로운_평가1_점수 = 4.5d;
    private double 구매자2_새로운_평가2_점수 = 5.0d;
    private double 구매자3_새로운_평가1_점수 = 3.0d;
    private double 구매자4_새로운_평가1_점수 = 3.5d;
    private double 구매자5_새로운_평가1_점수 = 1.0d;
    private double 구매자5_새로운_평가2_점수 = 2.0d;
    private double 구매자5_새로운_평가3_점수 = 3.0d;
    protected Long 기존에_적용된_평가_중_마지막_평가의_아이디 = 10L;
    protected Reliability 구매자1_최종_신뢰도 = new Reliability(4.0d);
    protected Reliability 구매자2_최종_신뢰도 = new Reliability(4.3d);
    protected Reliability 구매자3_최종_신뢰도 = new Reliability(4.25d);
    protected Reliability 구매자4_최종_신뢰도 = new Reliability(3.5d);
    protected Reliability 구매자5_최종_신뢰도 = new Reliability(2.0d);
    protected User 구매자1_기존_평가_2개_새로운_평가_1개;
    protected User 구매자2_기존_평가_3개_새로운_평가_2개;
    protected User 구매자3_기존_평가_5개_새로운_평가_1개;
    protected User 구매자4_기존_평가_없고_새로운_평가_1개;
    protected User 구매자5_기존_평가_없고_새로운_평가_3개;
    protected User 구매자6_기존_평가_없고_새로운_평가_없음;

    protected int 구매자1_최종_신뢰도_반영_개수 = 3;
    protected int 구매자2_최종_신뢰도_반영_개수 = 5;
    protected int 구매자3_최종_신뢰도_반영_개수 = 6;
    protected int 구매자4_최종_신뢰도_반영_개수 = 1;
    protected int 구매자5_최종_신뢰도_반영_개수 = 3;
    protected Review 구매자1_새로운_평가1;
    protected Review 구매자2_새로운_평가1;
    protected Review 구매자2_새로운_평가2;
    protected Review 구매자3_새로운_평가1;
    protected Review 구매자4_새로운_평가1;
    protected Review 구매자5_새로운_평가1;
    protected Review 구매자5_새로운_평가2;
    protected Review 구매자5_새로운_평가3;
    protected Long 새로운_평가를_업데이트한_후의_마지막으로_적용된_평가의_아이디 = 18L;


    @BeforeEach
    void setUp() {
        final User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(new ProfileImage("upload.png", "updateImage.png"))
                             .reliability(Reliability.INITIAL_RELIABILITY)
                             .oauthId("1000")
                             .build();
        구매자1_기존_평가_2개_새로운_평가_1개 = User.builder()
                                      .name("구매자1")
                                      .profileImage(new ProfileImage("upload.png", "updateImage.png"))
                                      .reliability(Reliability.INITIAL_RELIABILITY)
                                      .oauthId("1001")
                                      .build();
        구매자2_기존_평가_3개_새로운_평가_2개 = User.builder()
                                      .name("구매자2")
                                      .profileImage(new ProfileImage("upload.png", "updateImage.png"))
                                      .reliability(Reliability.INITIAL_RELIABILITY)
                                      .oauthId("1002")
                                      .build();
        구매자3_기존_평가_5개_새로운_평가_1개 = User.builder()
                                      .name("구매자3")
                                      .profileImage(new ProfileImage("upload.png", "updateImage.png"))
                                      .reliability(Reliability.INITIAL_RELIABILITY)
                                      .oauthId("1003")
                                      .build();
        구매자4_기존_평가_없고_새로운_평가_1개 = User.builder()
                                      .name("구매자4")
                                      .profileImage(new ProfileImage("upload.png", "updateImage.png"))
                                      .reliability(Reliability.INITIAL_RELIABILITY)
                                      .oauthId("1004")
                                      .build();
        구매자5_기존_평가_없고_새로운_평가_3개 = User.builder()
                                      .name("구매자5")
                                      .profileImage(new ProfileImage("upload.png", "updateImage.png"))
                                      .reliability(Reliability.INITIAL_RELIABILITY)
                                      .oauthId("1005")
                                      .build();
        구매자6_기존_평가_없고_새로운_평가_없음 = User.builder()
                                      .name("구매자6")
                                      .profileImage(new ProfileImage("upload.png", "updateImage.png"))
                                      .reliability(Reliability.INITIAL_RELIABILITY)
                                      .oauthId("1006")
                                      .build();
        userRepository.saveAll(List.of(판매자,
                구매자1_기존_평가_2개_새로운_평가_1개,
                구매자2_기존_평가_3개_새로운_평가_2개,
                구매자3_기존_평가_5개_새로운_평가_1개,
                구매자4_기존_평가_없고_새로운_평가_1개,
                구매자5_기존_평가_없고_새로운_평가_3개,
                구매자6_기존_평가_없고_새로운_평가_없음
        ));
        final Auction 경매1 = Auction.builder()
                                   .title("경매1")
                                   .seller(판매자)
                                   .build();
        final Auction 경매2 = Auction.builder()
                                   .title("경매2")
                                   .seller(판매자)
                                   .build();
        final Auction 경매3 = Auction.builder()
                                   .title("경매3")
                                   .seller(판매자)
                                   .build();
        final Auction 경매4 = Auction.builder()
                                   .title("경매4")
                                   .seller(판매자)
                                   .build();
        final Auction 경매5 = Auction.builder()
                                   .title("경매5")
                                   .seller(판매자)
                                   .build();
        final Auction 경매6 = Auction.builder()
                                   .title("경매7")
                                   .seller(판매자)
                                   .build();
        final Auction 경매7 = Auction.builder()
                                   .title("경매7")
                                   .seller(판매자)
                                   .build();
        final Auction 경매8 = Auction.builder()
                                   .title("경매8")
                                   .seller(판매자)
                                   .build();
        final Auction 경매9 = Auction.builder()
                                   .title("경매9")
                                   .seller(판매자)
                                   .build();
        final Auction 경매10 = Auction.builder()
                                    .title("경매10")
                                    .seller(판매자)
                                    .build();
        final Auction 경매11 = Auction.builder()
                                    .title("경매11")
                                    .seller(판매자)
                                    .build();
        final Auction 경매12 = Auction.builder()
                                    .title("경매12")
                                    .seller(판매자)
                                    .build();
        final Auction 경매13 = Auction.builder()
                                    .title("경매13")
                                    .seller(판매자)
                                    .build();
        final Auction 경매14 = Auction.builder()
                                    .title("경매14")
                                    .seller(판매자)
                                    .build();
        final Auction 경매15 = Auction.builder()
                                    .title("경매15")
                                    .seller(판매자)
                                    .build();
        final Auction 경매16 = Auction.builder()
                                    .title("경매16")
                                    .seller(판매자)
                                    .build();
        final Auction 경매17 = Auction.builder()
                                    .title("경매17")
                                    .seller(판매자)
                                    .build();
        final Auction 경매18 = Auction.builder()
                                    .title("경매18")
                                    .seller(판매자)
                                    .build();

        auctionRepository.save(경매1);
        auctionRepository.save(경매2);
        auctionRepository.save(경매3);
        auctionRepository.save(경매4);
        auctionRepository.save(경매5);
        auctionRepository.save(경매6);
        auctionRepository.save(경매7);
        auctionRepository.save(경매8);
        auctionRepository.save(경매9);
        auctionRepository.save(경매10);
        auctionRepository.save(경매11);
        auctionRepository.save(경매12);
        auctionRepository.save(경매13);
        auctionRepository.save(경매14);
        auctionRepository.save(경매15);
        auctionRepository.save(경매16);
        auctionRepository.save(경매17);
        auctionRepository.save(경매18);

        final Review 구매자1_기존_평가1 = Review.builder()
                                         .auction(경매1)
                                         .writer(판매자)
                                         .target(구매자1_기존_평가_2개_새로운_평가_1개)
                                         .score(new Score(구매자1_기존_신뢰도_점수))
                                         .build();
        final Review 구매자1_기존_평가2 = Review.builder()
                                         .auction(경매2)
                                         .writer(판매자)
                                         .target(구매자1_기존_평가_2개_새로운_평가_1개)
                                         .score(new Score(구매자1_기존_신뢰도_점수))
                                         .build();
        final Review 구매자2_기존_평가1 = Review.builder()
                                         .auction(경매3)
                                         .writer(판매자)
                                         .target(구매자2_기존_평가_3개_새로운_평가_2개)
                                         .score(new Score(구매자2_기존_신뢰도_점수))
                                         .build();
        final Review 구매자2_기존_평가2 = Review.builder()
                                         .auction(경매4)
                                         .writer(판매자)
                                         .target(구매자2_기존_평가_3개_새로운_평가_2개)
                                         .score(new Score(구매자2_기존_신뢰도_점수))
                                         .build();
        final Review 구매자2_기존_평가3 = Review.builder()
                                         .auction(경매5)
                                         .writer(판매자)
                                         .target(구매자2_기존_평가_3개_새로운_평가_2개)
                                         .score(new Score(구매자2_기존_신뢰도_점수))
                                         .build();
        final Review 구매자3_기존_평가1 = Review.builder()
                                         .auction(경매6)
                                         .writer(판매자)
                                         .target(구매자3_기존_평가_5개_새로운_평가_1개)
                                         .score(new Score(구매자3_기존_신뢰도_점수))
                                         .build();
        final Review 구매자3_기존_평가2 = Review.builder()
                                         .auction(경매7)
                                         .writer(판매자)
                                         .target(구매자3_기존_평가_5개_새로운_평가_1개)
                                         .score(new Score(구매자3_기존_신뢰도_점수))
                                         .build();
        final Review 구매자3_기존_평가3 = Review.builder()
                                         .auction(경매8)
                                         .writer(판매자)
                                         .target(구매자3_기존_평가_5개_새로운_평가_1개)
                                         .score(new Score(구매자3_기존_신뢰도_점수))
                                         .build();
        final Review 구매자3_기존_평가4 = Review.builder()
                                         .auction(경매9)
                                         .writer(판매자)
                                         .target(구매자3_기존_평가_5개_새로운_평가_1개)
                                         .score(new Score(구매자3_기존_신뢰도_점수))
                                         .build();
        final Review 구매자3_기존_평가5 = Review.builder()
                                         .auction(경매10)
                                         .writer(판매자)
                                         .target(구매자3_기존_평가_5개_새로운_평가_1개)
                                         .score(new Score(구매자3_기존_신뢰도_점수))
                                         .build();
        reviewRepository.saveAll(List.of(구매자1_기존_평가1, 구매자1_기존_평가2, 구매자2_기존_평가1, 구매자2_기존_평가2, 구매자2_기존_평가3, 구매자3_기존_평가1, 구매자3_기존_평가2, 구매자3_기존_평가3, 구매자3_기존_평가4, 구매자3_기존_평가5
        ));

        final UserReliability 구매자1_기존_신뢰도_반영_정보 = new UserReliability(구매자1_기존_평가_2개_새로운_평가_1개);
        구매자1_기존_신뢰도_반영_정보.updateReliability(new Reviews(List.of(구매자1_기존_평가1, 구매자1_기존_평가2)));

        final UserReliability 구매자2_기존_신뢰도_반영_정보 = new UserReliability(구매자2_기존_평가_3개_새로운_평가_2개);
        구매자2_기존_신뢰도_반영_정보.updateReliability(new Reviews(List.of(구매자2_기존_평가1, 구매자2_기존_평가2, 구매자2_기존_평가3)));

        final UserReliability 구매자3_기존_신뢰도_반영_정보 = new UserReliability(구매자3_기존_평가_5개_새로운_평가_1개);
        구매자3_기존_신뢰도_반영_정보.updateReliability(new Reviews(List.of(구매자3_기존_평가1, 구매자3_기존_평가2, 구매자3_기존_평가3, 구매자3_기존_평가4, 구매자3_기존_평가5)));

        userReliabilityRepository.saveAll(List.of(구매자1_기존_신뢰도_반영_정보, 구매자2_기존_신뢰도_반영_정보, 구매자3_기존_신뢰도_반영_정보));

        reliabilityUpdateHistoryRepository.save(new ReliabilityUpdateHistory(기존에_적용된_평가_중_마지막_평가의_아이디));

        구매자1_새로운_평가1 = Review.builder()
                             .auction(경매11)
                             .writer(판매자)
                             .target(구매자1_기존_평가_2개_새로운_평가_1개)
                             .score(new Score(구매자1_새로운_평가1_점수))
                             .build();
        구매자2_새로운_평가1 = Review.builder()
                             .auction(경매12)
                             .writer(판매자)
                             .target(구매자2_기존_평가_3개_새로운_평가_2개)
                             .score(new Score(구매자2_새로운_평가1_점수))
                             .build();
        구매자2_새로운_평가2 = Review.builder()
                             .auction(경매13)
                             .writer(판매자)
                             .target(구매자2_기존_평가_3개_새로운_평가_2개)
                             .score(new Score(구매자2_새로운_평가2_점수))
                             .build();
        구매자3_새로운_평가1 = Review.builder()
                             .auction(경매14)
                             .writer(판매자)
                             .target(구매자3_기존_평가_5개_새로운_평가_1개)
                             .score(new Score(구매자3_새로운_평가1_점수))
                             .build();
        구매자4_새로운_평가1 = Review.builder()
                             .auction(경매15)
                             .writer(판매자)
                             .target(구매자4_기존_평가_없고_새로운_평가_1개)
                             .score(new Score(구매자4_새로운_평가1_점수))
                             .build();
        구매자5_새로운_평가1 = Review.builder()
                             .auction(경매16)
                             .writer(판매자)
                             .target(구매자5_기존_평가_없고_새로운_평가_3개)
                             .score(new Score(구매자5_새로운_평가1_점수))
                             .build();
        구매자5_새로운_평가2 = Review.builder()
                             .auction(경매17)
                             .writer(판매자)
                             .target(구매자5_기존_평가_없고_새로운_평가_3개)
                             .score(new Score(구매자5_새로운_평가2_점수))
                             .build();
        구매자5_새로운_평가3 = Review.builder()
                             .auction(경매18)
                             .writer(판매자)
                             .target(구매자5_기존_평가_없고_새로운_평가_3개)
                             .score(new Score(구매자5_새로운_평가3_점수))
                             .build();
        reviewRepository.saveAll(List.of(구매자1_새로운_평가1, 구매자2_새로운_평가1, 구매자2_새로운_평가2, 구매자3_새로운_평가1, 구매자4_새로운_평가1, 구매자5_새로운_평가1, 구매자5_새로운_평가2, 구매자5_새로운_평가3
        ));
    }
}
