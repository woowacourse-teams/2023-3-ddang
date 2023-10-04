package com.ddang.ddang.auction.application.fixture;

import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionServiceFixture {

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaRegionRepository regionRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    @Autowired
    private JpaBidRepository bidRepository;

    private Category 가구_카테고리 = new Category("가구");
    private Category 가구_서브_의자_카테고리 = new Category("의자");

    protected User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(new ProfileImage("upload.png", "store.png"))
                             .reliability(4.7d)
                             .oauthId("12345")
                             .build();
    protected User 구매자 = User.builder()
                             .name("구매자")
                             .profileImage(new ProfileImage("upload.png", "store.png"))
                             .reliability(4.7d)
                             .oauthId("54321")
                             .build();
    protected User 신뢰도가_null인_판매자 = User.builder()
                                        .name("신뢰도가 null인 판매자")
                                        .profileImage(new ProfileImage("upload.png", "store.png"))
                                        .reliability(null)
                                        .oauthId("99999")
                                        .build();

    private MockMultipartFile 경매_이미지_파일 = new MockMultipartFile(
            "image.png",
            "image.png",
            MediaType.IMAGE_PNG.toString(),
            new byte[]{1}
    );

    private Long 존재하지_않는_지역_ID = -999L;
    private Long 존재하지_않는_카테고리_ID = -999L;
    private BidPrice 입찰이_존재하는_경매_입찰_가격 = new BidPrice(1_000);

    protected Long 존재하지_않는_사용자_ID = -999L;
    protected Long 존재하지_않는_경매_ID = -999L;
    protected StoreImageDto 경매_이미지_엔티티 = new StoreImageDto("upload.png", "store.png");
    protected CreateAuctionDto 유효한_경매_생성_dto;
    protected CreateAuctionDto 존재하지_않는_판매자의_경매_생성_dto;
    protected CreateAuctionDto 존재하지_않는_지역의_경매_생성_dto;
    protected CreateAuctionDto 두_번째_지역의_경매_생성_dto;
    protected CreateAuctionDto 존재하지_않는_카테고리의_경매_생성_dto;
    protected CreateAuctionDto 메인_카테고리의_경매_생성_dto;
    protected CreateAuctionDto 종료되는_날이_3일_뒤인_경매_생성_dto;
    protected CreateAuctionDto 종료된_경매_생성_dto;
    protected Auction 채팅방이_있는_경매;
    protected Auction 종료되는_날이_3일_뒤인_경매;
    protected Auction 입찰이_존재하는_경매;
    protected Auction 종료된_경매;
    protected Auction 신뢰도가_null인_판매자의_경매;
    protected BidPrice 채팅방이_있는_경매_입찰_가격 = new BidPrice(10_000);
    protected Bid 채팅방이_있는_경매_입찰;
    protected Bid 입찰이_존재하는_경매_입찰;

    @BeforeEach
    void setUp() {
        final Region 서울특별시 = new Region("서울특별시");
        final Region 강남구 = new Region("강남구");
        final Region 역삼동 = new Region("역삼동");

        서울특별시.addSecondRegion(강남구);
        강남구.addThirdRegion(역삼동);

        regionRepository.save(서울특별시);

        가구_카테고리.addSubCategory(가구_서브_의자_카테고리);

        categoryRepository.save(가구_카테고리);

        userRepository.saveAll(List.of(판매자, 구매자, 신뢰도가_null인_판매자));

        유효한_경매_생성_dto = new CreateAuctionDto(
                "제목",
                "내용",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(역삼동.getId()),
                가구_서브_의자_카테고리.getId(),
                List.of(경매_이미지_파일),
                판매자.getId()
        );
        종료된_경매_생성_dto = new CreateAuctionDto(
                "제목",
                "내용",
                1_000,
                1_000,
                LocalDateTime.now().minusDays(3),
                List.of(역삼동.getId()),
                가구_서브_의자_카테고리.getId(),
                List.of(경매_이미지_파일),
                판매자.getId()
        );
        유효한_경매_생성_dto = new CreateAuctionDto(
                "제목",
                "내용",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(역삼동.getId()),
                가구_서브_의자_카테고리.getId(),
                List.of(경매_이미지_파일),
                판매자.getId()
        );
        존재하지_않는_판매자의_경매_생성_dto = new CreateAuctionDto(
                "제목",
                "내용",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(역삼동.getId()),
                가구_서브_의자_카테고리.getId(),
                List.of(경매_이미지_파일),
                존재하지_않는_사용자_ID
        );
        존재하지_않는_지역의_경매_생성_dto = new CreateAuctionDto(
                "제목",
                "내용",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(존재하지_않는_지역_ID),
                가구_서브_의자_카테고리.getId(),
                List.of(경매_이미지_파일),
                판매자.getId()
        );
        두_번째_지역의_경매_생성_dto = new CreateAuctionDto(
                "제목",
                "내용",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(강남구.getId()),
                가구_서브_의자_카테고리.getId(),
                List.of(경매_이미지_파일),
                판매자.getId()
        );
        존재하지_않는_카테고리의_경매_생성_dto = new CreateAuctionDto(
                "제목",
                "내용",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(역삼동.getId()),
                존재하지_않는_카테고리_ID,
                List.of(경매_이미지_파일),
                판매자.getId()
        );
        메인_카테고리의_경매_생성_dto = new CreateAuctionDto(
                "제목",
                "내용",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(역삼동.getId()),
                가구_카테고리.getId(),
                List.of(경매_이미지_파일),
                판매자.getId()
        );
        종료되는_날이_3일_뒤인_경매_생성_dto = new CreateAuctionDto(
                "제목",
                "내용",
                1_000,
                1_000,
                LocalDateTime.now().plusDays(3),
                List.of(역삼동.getId()),
                가구_서브_의자_카테고리.getId(),
                List.of(경매_이미지_파일),
                판매자.getId()
        );

        채팅방이_있는_경매 = 유효한_경매_생성_dto.toEntity(판매자, 가구_서브_의자_카테고리);
        종료되는_날이_3일_뒤인_경매 = 종료되는_날이_3일_뒤인_경매_생성_dto.toEntity(판매자, 가구_서브_의자_카테고리);
        입찰이_존재하는_경매 = 유효한_경매_생성_dto.toEntity(판매자, 가구_서브_의자_카테고리);
        종료된_경매 = 유효한_경매_생성_dto.toEntity(판매자, 가구_서브_의자_카테고리);

        채팅방이_있는_경매_입찰 = new Bid(채팅방이_있는_경매, 구매자, 채팅방이_있는_경매_입찰_가격);
        입찰이_존재하는_경매_입찰 = new Bid(입찰이_존재하는_경매, 구매자, 입찰이_존재하는_경매_입찰_가격);

        bidRepository.saveAll(List.of(채팅방이_있는_경매_입찰, 입찰이_존재하는_경매_입찰));

        채팅방이_있는_경매.updateLastBid(채팅방이_있는_경매_입찰);
        입찰이_존재하는_경매.updateLastBid(입찰이_존재하는_경매_입찰);

        신뢰도가_null인_판매자의_경매 = Auction.builder()
                                    .title("신뢰도가 null인 판매자의 경매")
                                    .description("신뢰도가 null인 판매자의 경매")
                                    .subCategory(가구_서브_의자_카테고리)
                                    .seller(신뢰도가_null인_판매자)
                                    .bidUnit(new BidUnit(1_000))
                                    .startPrice(new Price(10_000))
                                    .closingTime(LocalDateTime.now().plusDays(3L))
                                    .build();
        신뢰도가_null인_판매자의_경매.addAuctionImages(List.of(new AuctionImage("auction.png", "auction.png")));
        auctionRepository.saveAll(List.of(신뢰도가_null인_판매자의_경매, 채팅방이_있는_경매, 종료되는_날이_3일_뒤인_경매, 입찰이_존재하는_경매, 종료된_경매));

        final ChatRoom 채팅방 = new ChatRoom(채팅방이_있는_경매, 구매자);

        chatRoomRepository.save(채팅방);
    }
}
