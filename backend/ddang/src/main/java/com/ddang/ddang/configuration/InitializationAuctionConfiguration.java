package com.ddang.ddang.configuration;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@ConditionalOnProperty(name = "data.init.auction.enabled", havingValue = "true")
@RequiredArgsConstructor
public class InitializationAuctionConfiguration implements ApplicationRunner {

    private final JpaAuctionRepository auctionRepository;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        final Auction auction1 = Auction.builder()
                                        .title("맥북 2021 13인치")
                                        .description("맥북 2021 13인치 팝니다. 애플 감성 안 맞아요.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000_000))
                                        .closingTime(LocalDateTime.now())
                                        .image("https://www.apple.com/newsroom/images/product/mac/standard/Apple_MacBook-Pro_14-16-inch_10182021_big.jpg.large.jpg")
                                        .firstRegion("서울특별시")
                                        .secondRegion("강남구")
                                        .thirdRegion("역삼동")
                                        .mainCategory("전자기기")
                                        .subCategory("노트북")
                                        .build();

        final Auction auction2 = Auction.builder()
                                        .title("맥북 16인치")
                                        .description("맥북 2019 16인치 팝니다. 급전이 필요해요...")
                                        .bidUnit(new BidUnit(3_000))
                                        .startPrice(new Price(900_000))
                                        .closingTime(LocalDateTime.now()
                                                                  .plusDays(5L))
                                        .image("https://www.apple.com/newsroom/images/product/mac/standard/Apple_MacBook-Pro_14-16-inch_10182021_big.jpg.large.jpg")
                                        .mainCategory("전자기기")
                                        .subCategory("노트북")
                                        .build();

        auctionRepository.save(auction1);
        auctionRepository.save(auction2);
    }
}
