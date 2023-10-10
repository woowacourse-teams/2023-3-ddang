package com.ddang.ddang.report.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaChatRoomReportRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class JpaChatRoomReportRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaProfileImageRepository profileImageRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaChatRoomReportRepository chatRoomReportRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    protected Long 존재하지_않는_채팅방_아이디 = -9999L;
    protected Long 존재하지_않는_사용자_아이디 = -9999L;
    protected User 구매자1;
    protected User 구매자1겸_신고자;
    protected ChatRoom 채팅방1;
    protected ChatRoomReport 채팅방_신고1;
    protected ChatRoomReport 채팅방_신고2;
    protected ChatRoomReport 채팅방_신고3;

    @BeforeEach
    void setUp() {
        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        final User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(프로필_이미지)
                             .reliability(new Reliability(4.7d))
                             .oauthId("12345")
                             .build();
        구매자1 = User.builder()
                   .name("구매자1")
                   .profileImage(프로필_이미지)
                   .reliability(new Reliability(4.7d))
                   .oauthId("12346")
                   .build();
        final User 구매자2겸_신고자 = User.builder()
                                   .name("구매자2")
                                   .profileImage(프로필_이미지)
                                   .reliability(new Reliability(4.7d))
                                   .oauthId("12347")
                                   .build();
        final User 구매자3겸_신고자 = User.builder()
                                   .name("구매자3")
                                   .profileImage(프로필_이미지)
                                   .reliability(new Reliability(4.7d))
                                   .oauthId("12348")
                                   .build();
        구매자1겸_신고자 = 구매자1;

        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        final AuctionImage 경매_이미지 = new AuctionImage("경매이미지.jpg", "경매이미지.jpg");
        final Auction 경매1 = Auction.builder()
                                   .seller(판매자)
                                   .title("경매 상품")
                                   .description("이것은 경매 상품입니다.")
                                   .subCategory(전자기기_서브_노트북_카테고리)
                                   .bidUnit(new BidUnit(1_000))
                                   .startPrice(new Price(1_000))
                                   .closingTime(LocalDateTime.now())
                                   .build();
        경매1.addAuctionImages(List.of(경매_이미지));
        final Auction 경매2 = Auction.builder()
                                   .seller(판매자)
                                   .title("경매 상품")
                                   .description("이것은 경매 상품입니다.")
                                   .subCategory(전자기기_서브_노트북_카테고리)
                                   .bidUnit(new BidUnit(1_000))
                                   .startPrice(new Price(1_000))
                                   .closingTime(LocalDateTime.now())
                                   .build();
        경매2.addAuctionImages(List.of(경매_이미지));
        final Auction 경매3 = Auction.builder()
                                   .seller(판매자)
                                   .title("경매 상품")
                                   .description("이것은 경매 상품입니다.")
                                   .subCategory(전자기기_서브_노트북_카테고리)
                                   .bidUnit(new BidUnit(1_000))
                                   .startPrice(new Price(1_000))
                                   .closingTime(LocalDateTime.now())
                                   .build();
        경매3.addAuctionImages(List.of(경매_이미지));

        채팅방1 = new ChatRoom(경매1, 구매자1);
        final ChatRoom 채팅방2 = new ChatRoom(경매2, 구매자2겸_신고자);
        final ChatRoom 채팅방3 = new ChatRoom(경매3, 구매자3겸_신고자);

        채팅방_신고1 = new ChatRoomReport(구매자1겸_신고자, 채팅방1, "신고합니다.");
        채팅방_신고2 = new ChatRoomReport(구매자2겸_신고자, 채팅방2, "신고합니다.");
        채팅방_신고3 = new ChatRoomReport(구매자3겸_신고자, 채팅방3, "신고합니다.");

        profileImageRepository.save(프로필_이미지);
        userRepository.saveAll(List.of(판매자, 구매자1, 구매자2겸_신고자, 구매자3겸_신고자));

        categoryRepository.saveAll(List.of(전자기기_카테고리, 전자기기_서브_노트북_카테고리));
        auctionImageRepository.save(경매_이미지);
        auctionRepository.saveAll(List.of(경매1, 경매2, 경매3));

        chatRoomRepository.saveAll(List.of(채팅방1, 채팅방2, 채팅방3));

        chatRoomReportRepository.saveAll(List.of(채팅방_신고1, 채팅방_신고2, 채팅방_신고3));

        em.flush();
        em.clear();
    }
}
