package com.ddang.ddang.configuration;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import java.time.LocalDateTime;
import java.util.List;
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
    private final JpaRegionRepository regionRepository;
    private final JpaCategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        final Region firstRegion = regionRepository.findById(1L).get();
        final Region secondRegion = regionRepository.findById(2L).get();
        final Region thirdRegion = regionRepository.findById(3L).get();

        secondRegion.addThirdRegion(thirdRegion);
        firstRegion.addSecondRegion(secondRegion);

        final AuctionRegion auctionRegion1 = new AuctionRegion(thirdRegion);
        final AuctionRegion auctionRegion2 = new AuctionRegion(thirdRegion);

        final Category main = new Category("전자기기");
        final Category sub = new Category("노트북");

        main.addSubCategory(sub);

        categoryRepository.save(main);

        final Auction auction1 = Auction.builder()
                                        .title("맥북 2021 13인치")
                                        .description("맥북 2021 13인치 팝니다. 애플 감성 안 맞아요.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000_000))
                                        .closingTime(LocalDateTime.now())
                                        .subCategory(sub)
                                        .build();
        auction1.addAuctionRegions(List.of(auctionRegion1));

        final Auction auction2 = Auction.builder()
                                        .title("맥북 16인치")
                                        .description("맥북 2019 16인치 팝니다. 급전이 필요해요...")
                                        .bidUnit(new BidUnit(3_000))
                                        .startPrice(new Price(900_000))
                                        .closingTime(LocalDateTime.now()
                                                                  .plusDays(5L))
                                        .subCategory(sub)
                                        .build();
        auction2.addAuctionRegions(List.of(auctionRegion2));

        auctionRepository.save(auction1);
        auctionRepository.save(auction2);
    }
}
