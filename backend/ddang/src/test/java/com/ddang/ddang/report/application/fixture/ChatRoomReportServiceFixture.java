package com.ddang.ddang.report.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import com.ddang.ddang.report.application.dto.CreateChatRoomReportDto;
import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaChatRoomReportRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ChatRoomReportServiceFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaProfileImageRepository profileImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private JpaChatRoomReportRepository chatRoomReportRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    protected User 이미_신고한_구매자1;
    protected User 이미_신고한_구매자2;
    protected User 이미_신고한_구매자3;
    protected ChatRoom 채팅방1;
    protected ChatRoom 채팅방2;
    protected ChatRoom 채팅방3;

    protected CreateChatRoomReportDto 채팅방_신고_요청_dto;
    protected CreateChatRoomReportDto 존재하지_않는_사용자의_채팅방_신고_요청_dto;
    protected CreateChatRoomReportDto 존재하지_않는_채팅방_신고_요청_dto;
    protected CreateChatRoomReportDto 참여자가_아닌_사용자의_채팅방_신고_요청_dto;
    protected CreateChatRoomReportDto 이미_신고한_사용자의_채팅방_신고_요청_dto;

    @BeforeEach
    void setUp() {
        final Long 존재하지_않는_사용자_아이디 = -9999L;
        final Long 존재하지_않는_채팅방_아이디 = -9999L;

        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        final User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(프로필_이미지)
                             .reliability(new Reliability(4.7d))
                             .oauthId("12345")
                             .build();
        final User 판매자겸_아직_신고하지_않은_신고자 = 판매자;
        이미_신고한_구매자1 = User.builder()
                          .name("구매자1")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12346")
                          .build();
        이미_신고한_구매자2 = User.builder()
                          .name("구매자2")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12347")
                          .build();
        이미_신고한_구매자3 = User.builder()
                          .name("구매자3")
                          .profileImage(프로필_이미지)
                          .reliability(new Reliability(4.7d))
                          .oauthId("12348")
                          .build();
        final User 채팅방_참여자가_아닌_사용자 = User.builder()
                                         .name("채팅방_참여자가_아닌_사용자")
                                         .profileImage(프로필_이미지)
                                         .reliability(new Reliability(4.7d))
                                         .oauthId("12349")
                                         .build();

        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        final AuctionImage 경매_이미지 = new AuctionImage("경매이미지.jpg", "경매이미지.jpg");
        final Auction 경매1 = Auction.builder()
                                   .seller(판매자겸_아직_신고하지_않은_신고자)
                                   .title("경매 상품")
                                   .description("이것은 경매 상품입니다.")
                                   .subCategory(전자기기_서브_노트북_카테고리)
                                   .bidUnit(new BidUnit(1_000))
                                   .startPrice(new Price(1_000))
                                   .closingTime(LocalDateTime.now())
                                   .build();
        경매1.addAuctionImages(List.of(경매_이미지));
        final Auction 경매2 = Auction.builder()
                                   .seller(판매자겸_아직_신고하지_않은_신고자)
                                   .title("경매 상품")
                                   .description("이것은 경매 상품입니다.")
                                   .subCategory(전자기기_서브_노트북_카테고리)
                                   .bidUnit(new BidUnit(1_000))
                                   .startPrice(new Price(1_000))
                                   .closingTime(LocalDateTime.now())
                                   .build();
        경매2.addAuctionImages(List.of(경매_이미지));
        final Auction 경매3 = Auction.builder()
                                   .seller(판매자겸_아직_신고하지_않은_신고자)
                                   .title("경매 상품")
                                   .description("이것은 경매 상품입니다.")
                                   .subCategory(전자기기_서브_노트북_카테고리)
                                   .bidUnit(new BidUnit(1_000))
                                   .startPrice(new Price(1_000))
                                   .closingTime(LocalDateTime.now())
                                   .build();
        경매3.addAuctionImages(List.of(경매_이미지));

        채팅방1 = new ChatRoom(경매1, 이미_신고한_구매자1);
        채팅방2 = new ChatRoom(경매2, 이미_신고한_구매자2);
        채팅방3 = new ChatRoom(경매3, 이미_신고한_구매자3);

        final ChatRoomReport 채팅방_신고1 = new ChatRoomReport(이미_신고한_구매자1, 채팅방1, "신고합니다.");
        final ChatRoomReport 채팅방_신고2 = new ChatRoomReport(이미_신고한_구매자2, 채팅방2, "신고합니다.");
        final ChatRoomReport 채팅방_신고3 = new ChatRoomReport(이미_신고한_구매자3, 채팅방3, "신고합니다.");

        profileImageRepository.save(프로필_이미지);

        userRepository.save(판매자겸_아직_신고하지_않은_신고자);
        userRepository.save(이미_신고한_구매자1);
        userRepository.save(이미_신고한_구매자2);
        userRepository.save(이미_신고한_구매자3);
        userRepository.save(채팅방_참여자가_아닌_사용자);

        categoryRepository.saveAll(List.of(전자기기_카테고리, 전자기기_서브_노트북_카테고리));
        auctionImageRepository.save(경매_이미지);
        auctionRepository.save(경매1);
        auctionRepository.save(경매2);
        auctionRepository.save(경매3);

        chatRoomRepository.saveAll(List.of(채팅방1, 채팅방2, 채팅방3));

        chatRoomReportRepository.saveAll(List.of(채팅방_신고1, 채팅방_신고2, 채팅방_신고3));

        채팅방_신고_요청_dto = new CreateChatRoomReportDto(
                채팅방1.getId(),
                "신고합니다.",
                판매자겸_아직_신고하지_않은_신고자.getId()
        );
        존재하지_않는_사용자의_채팅방_신고_요청_dto = new CreateChatRoomReportDto(
                채팅방1.getId(),
                "신고합니다.",
                존재하지_않는_사용자_아이디
        );
        존재하지_않는_채팅방_신고_요청_dto = new CreateChatRoomReportDto(
                존재하지_않는_채팅방_아이디,
                "신고합니다.",
                판매자겸_아직_신고하지_않은_신고자.getId()
        );
        참여자가_아닌_사용자의_채팅방_신고_요청_dto = new CreateChatRoomReportDto(
                채팅방1.getId(),
                "신고합니다.",
                채팅방_참여자가_아닌_사용자.getId()
        );
        이미_신고한_사용자의_채팅방_신고_요청_dto = new CreateChatRoomReportDto(
                채팅방1.getId(),
                "신고합니다.",
                이미_신고한_구매자1.getId()
        );
    }
}
