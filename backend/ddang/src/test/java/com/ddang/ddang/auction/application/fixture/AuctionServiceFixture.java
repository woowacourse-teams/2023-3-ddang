package com.ddang.ddang.auction.application.fixture;

import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionServiceFixture {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private JpaRegionRepository regionRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaBidRepository bidRepository;

    private Category 가구_카테고리 = new Category("가구");
    private Category 가구_서브_의자_카테고리 = new Category("의자");

    protected User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(new ProfileImage("upload.png", "store.png"))
                             .reliability(new Reliability(4.7d))
                             .oauthId("12345")
                             .build();
    protected User 구매자 = User.builder()
                             .name("구매자")
                             .profileImage(new ProfileImage("upload.png", "store.png"))
                             .reliability(new Reliability(4.7d))
                             .oauthId("54321")
                             .build();

    private MockMultipartFile 경매_이미지_파일 = new MockMultipartFile(
            "image.png",
            "image.png",
            MediaType.IMAGE_PNG.toString(),
            new byte[]{1}
    );

    private Long 존재하지_않는_지역_ID = -999L;
    private Long 존재하지_않는_카테고리_ID = -999L;
    private BidPrice 구매자가_입찰한_경매1_입찰_가격 = new BidPrice(1_000);

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
    protected Auction 종료되는_날이_3일_뒤인_경매;
    protected Auction 구매자가_입찰한_경매1;
    protected Auction 구매자가_입찰한_경매2;
    protected Auction 종료된_경매;
    private BidPrice 구매자가_입찰한_경매2_입찰_가격 = new BidPrice(10_000);
    private Bid 구매자가_입찰한_경매1_입찰;
    private Bid 구매자가_입찰한_경매2_입찰;

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

        userRepository.save(판매자);
        userRepository.save(구매자);

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

        구매자가_입찰한_경매2 = 유효한_경매_생성_dto.toEntity(판매자, 가구_서브_의자_카테고리);
        종료되는_날이_3일_뒤인_경매 = 종료되는_날이_3일_뒤인_경매_생성_dto.toEntity(판매자, 가구_서브_의자_카테고리);
        구매자가_입찰한_경매1 = 유효한_경매_생성_dto.toEntity(판매자, 가구_서브_의자_카테고리);
        종료된_경매 = 유효한_경매_생성_dto.toEntity(판매자, 가구_서브_의자_카테고리);

        구매자가_입찰한_경매1_입찰 = new Bid(구매자가_입찰한_경매1, 구매자, 구매자가_입찰한_경매1_입찰_가격);
        구매자가_입찰한_경매2_입찰 = new Bid(구매자가_입찰한_경매2, 구매자, 구매자가_입찰한_경매2_입찰_가격);

        bidRepository.saveAll(List.of(구매자가_입찰한_경매1_입찰, 구매자가_입찰한_경매2_입찰));

        구매자가_입찰한_경매1.updateLastBid(구매자가_입찰한_경매1_입찰);
        구매자가_입찰한_경매2.updateLastBid(구매자가_입찰한_경매2_입찰);

        구매자가_입찰한_경매1.addAuctionRegions(List.of(new AuctionRegion(역삼동)));
        구매자가_입찰한_경매2.addAuctionRegions(List.of(new AuctionRegion(역삼동)));
        종료되는_날이_3일_뒤인_경매.addAuctionRegions(List.of(new AuctionRegion(역삼동)));
        종료된_경매.addAuctionRegions(List.of(new AuctionRegion(역삼동)));

        auctionRepository.save(구매자가_입찰한_경매1);
        auctionRepository.save(구매자가_입찰한_경매2);
        auctionRepository.save(종료되는_날이_3일_뒤인_경매);
        auctionRepository.save(종료된_경매);
    }
}
