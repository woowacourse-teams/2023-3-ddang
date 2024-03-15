package com.ddang.ddang.bid.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.dto.AuctionAndImageDto;
import com.ddang.ddang.auction.domain.repository.AuctionAndImageRepository;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.repository.BidRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidConcurrencyServiceTest {

    @Autowired
    BidService bidService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    AuctionAndImageRepository auctionAndImageRepository;

    @Autowired
    BidRepository bidRepository;

    @Autowired
    PlatformTransactionManager transactionManager;

    private TransactionTemplate transactionTemplate;

    private String 이미지_절대_url = "https://3-ddang.store/auctions/images";
    private Auction 경매;
    private List<CreateBidDto> 동시_입찰_요청들;

    @BeforeEach
    void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        final User 판매자 = User.builder()
                             .name("판매자")
                             .reliability(new Reliability(4.7d))
                             .oauthId("12345")
                             .build();
        final User 기존_입찰자 = User.builder()
                                .name("기존 입찰자")
                                .reliability(new Reliability(4.7d))
                                .oauthId("12346")
                                .build();
        final User 입찰자1 = User.builder()
                              .name("입찰자1")
                              .reliability(new Reliability(4.7d))
                              .oauthId("12347")
                              .build();
        final User 입찰자2 = User.builder()
                              .name("입찰자2")
                              .reliability(new Reliability(4.7d))
                              .oauthId("12348")
                              .build();
        final User 입찰자3 = User.builder()
                              .name("입찰자3")
                              .reliability(new Reliability(4.7d))
                              .oauthId("12349")
                              .build();
        final User 입찰자4 = User.builder()
                              .name("입찰자4")
                              .reliability(new Reliability(4.7d))
                              .oauthId("12350")
                              .build();
        final User 입찰자5 = User.builder()
                              .name("입찰자5")
                              .reliability(new Reliability(4.7d))
                              .oauthId("12351")
                              .build();
        final User 입찰자6 = User.builder()
                              .name("입찰자6")
                              .reliability(new Reliability(4.7d))
                              .oauthId("12352")
                              .build();
        final User 입찰자7 = User.builder()
                              .name("입찰자7")
                              .reliability(new Reliability(4.7d))
                              .oauthId("12353")
                              .build();
        final User 입찰자8 = User.builder()
                              .name("입찰자8")
                              .reliability(new Reliability(4.7d))
                              .oauthId("12354")
                              .build();
        final User 입찰자9 = User.builder()
                              .name("입찰자9")
                              .reliability(new Reliability(4.7d))
                              .oauthId("12355")
                              .build();
        final User 입찰자10 = User.builder()
                               .name("입찰자10")
                               .reliability(new Reliability(4.7d))
                               .oauthId("12356")
                               .build();
        userRepository.save(판매자);
        userRepository.save(기존_입찰자);
        userRepository.save(입찰자1);
        userRepository.save(입찰자2);
        userRepository.save(입찰자3);
        userRepository.save(입찰자4);
        userRepository.save(입찰자5);
        userRepository.save(입찰자6);
        userRepository.save(입찰자7);
        userRepository.save(입찰자8);
        userRepository.save(입찰자9);
        userRepository.save(입찰자10);

        final AuctionImage 경매_이미지1 = new AuctionImage("auction_image.png", "auction_image.png");
        경매 = Auction.builder()
                    .seller(판매자)
                    .title("경매 상품 1")
                    .description("이것은 경매 상품 1 입니다.")
                    .bidUnit(new BidUnit(1_000))
                    .startPrice(new Price(1_000))
                    .closingTime(LocalDateTime.now().plusDays(7))
                    .build();
        경매.addAuctionImages(List.of(경매_이미지1));
        auctionRepository.save(경매);

//        bidService.create(new CreateBidDto(경매.getId(), 1_000, 기존_입찰자.getId()), 이미지_절대_url);

        CreateBidDto 동시_입찰_요청_dto1 = new CreateBidDto(경매.getId(), 10_000, 입찰자1.getId());
        CreateBidDto 동시_입찰_요청_dto2 = new CreateBidDto(경매.getId(), 10_000, 입찰자2.getId());
        CreateBidDto 동시_입찰_요청_dto3 = new CreateBidDto(경매.getId(), 10_000, 입찰자3.getId());
        CreateBidDto 동시_입찰_요청_dto4 = new CreateBidDto(경매.getId(), 10_000, 입찰자4.getId());
        CreateBidDto 동시_입찰_요청_dto5 = new CreateBidDto(경매.getId(), 10_000, 입찰자5.getId());
        CreateBidDto 동시_입찰_요청_dto6 = new CreateBidDto(경매.getId(), 10_000, 입찰자6.getId());
        CreateBidDto 동시_입찰_요청_dto7 = new CreateBidDto(경매.getId(), 10_000, 입찰자7.getId());
        CreateBidDto 동시_입찰_요청_dto8 = new CreateBidDto(경매.getId(), 10_000, 입찰자8.getId());
        CreateBidDto 동시_입찰_요청_dto9 = new CreateBidDto(경매.getId(), 10_000, 입찰자9.getId());
        CreateBidDto 동시_입찰_요청_dto10 = new CreateBidDto(경매.getId(), 10_000, 입찰자10.getId());
        동시_입찰_요청들 = List.of(동시_입찰_요청_dto1, 동시_입찰_요청_dto2, 동시_입찰_요청_dto3, 동시_입찰_요청_dto4);
//        동시_입찰_요청들 = List.of(
//                동시_입찰_요청_dto1,
//                동시_입찰_요청_dto2,
//                동시_입찰_요청_dto3,
//                동시_입찰_요청_dto4,
//                동시_입찰_요청_dto5,
//                동시_입찰_요청_dto6,
//                동시_입찰_요청_dto7,
//                동시_입찰_요청_dto8,
//                동시_입찰_요청_dto9,
//                동시_입찰_요청_dto10
//        );
    }

    @Test
    void 동시성_문제_테스트() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(동시_입찰_요청들.size());
        CountDownLatch latch = new CountDownLatch(동시_입찰_요청들.size());

//        for (CreateBidDto bidDto : 동시_입찰_요청들) {
//            bidService.create(bidDto, 이미지_절대_url);
//        }

        for (CreateBidDto bidDto : 동시_입찰_요청들) {
            executorService.submit(() -> {
                try {
                    bidService.create(bidDto, 이미지_절대_url);
                } catch (IllegalStateException e) {
                } finally {
                    latch.countDown();
                }
            });
        }

//        for (CreateBidDto bidDto : 동시_입찰_요청들) {
//            executorService.submit(() ->
//                    transactionTemplate.execute((status -> {
//                        bidService.create(bidDto, 이미지_절대_url);
//                        latch.countDown();
//                        return null;
//                    }))
//            );
//        }
        latch.await();

        final List<Bid> bids = bidRepository.findAllByAuctionId(경매.getId());
        System.out.println("final auction: " + auctionRepository.findAll());
        System.out.println("final bids: " + bids);
        final Optional<AuctionAndImageDto> auction = auctionAndImageRepository.findDtoByAuctionId(경매.getId());
        if (auction.isPresent()) {
            System.out.println("final auction: " + auction.get());
            System.out.println("final lastbid: " + auction.get().auction().getLastBid());
        } else {
            System.out.println("final auction: not found");
        }
    }
}
