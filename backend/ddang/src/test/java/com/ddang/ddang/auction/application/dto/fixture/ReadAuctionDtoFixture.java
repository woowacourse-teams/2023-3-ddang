package com.ddang.ddang.auction.application.dto.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ReadAuctionDtoFixture {

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaRegionRepository regionRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaUserRepository userRepository;

    private Category 가구_카테고리 = new Category("가구");
    private Category 가구_서브_의자_카테고리 = new Category("의자");
    protected User 신뢰도가_null인_판매자 = User.builder()
                                        .name("신뢰도가 null인 판매자")
                                        .profileImage(new ProfileImage("upload.png", "store.png"))
                                        .reliability(null)
                                        .oauthId("99999")
                                        .build();
    protected Auction 신뢰도가_null인_판매자의_경매;

    @BeforeEach
    void setUp() {
        userRepository.save(신뢰도가_null인_판매자);

        가구_카테고리.addSubCategory(가구_서브_의자_카테고리);
        categoryRepository.save(가구_카테고리);

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
        auctionRepository.save(신뢰도가_null인_판매자의_경매);
    }
}
