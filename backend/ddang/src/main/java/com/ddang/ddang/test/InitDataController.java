package com.ddang.ddang.test;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;
import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaAuctionReportRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class InitDataController {

    private final AuctionService auctionService;
    private final JpaUserRepository userRepository;
    private final JpaBidRepository bidRepository;
    private final JpaChatRoomRepository chatRoomRepository;
    private final JpaAuctionRepository auctionRepository;
    private final JpaRegionRepository regionRepository;
    private final JpaCategoryRepository categoryRepository;
    private final JpaAuctionReportRepository auctionReportRepository;
    private final StoreImageProcessor imageProcessor;

    @GetMapping("test/{auctionId}")
    public String read(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @PathVariable final Long auctionId
    ) {
        final ReadAuctionDto readAuctionDto = auctionService.readByAuctionId(auctionId);

        return "auctionId : " + auctionId;
    }

    @PostMapping("/init/users")
    public ResponseEntity<Void> users(
            @RequestPart final MultipartFile image
    ) throws IOException {
        long startTime = System.currentTimeMillis();

        final List<User> users = new ArrayList<>();

        Random random = new Random();
        double minValue = 0.0d;
        double maxValue = 5.0d;

        for (int i = 0; i < 100; i++) {
            final double randomValue = minValue + (maxValue - minValue) * random.nextDouble();
            final String uuid = UUID.randomUUID().toString();

            final User user = User.builder()
                                  .name("사용자 " + uuid)
                                  .profileImage(null)
                                  .reliability(randomValue)
                                  .oauthId(uuid)
                                  .build();

            users.add(user);

            if (i % 2 == 0) {
                user.update(user.getName(), convertProfileImage(image));
            }
        }

        userRepository.saveAll(users);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        long hours = executionTime / 3600000;
        long minutes = (executionTime % 3600000) / 60000;
        long seconds = (executionTime % 60000) / 1000;

        System.out.println("메서드 실행 시간: " + hours + " 시간 " + minutes + " 분 " + seconds + " 초");

        return ResponseEntity.noContent().build();
    }

    private ProfileImage convertProfileImage(final MultipartFile image) {
        return imageProcessor.storeImageFile(image).toProfileImageEntity();
    }

    @PostMapping("/init/auctions")
    public ResponseEntity<Void> auctions(
            @RequestPart final List<MultipartFile> images,
            @RequestPart @Valid final CreateAuctionRequest requests
    ) {
        Random random = new Random();

        int minValue = 1;
        int maxValue = 100;

        final List<Auction> auctions = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            System.out.printf("%d 번째 경매 저장\n", i);

            long randomValue = random.nextLong(maxValue - minValue + 1) + minValue;

            final CreateAuctionDto dto = CreateAuctionDto.of(
                    requests,
                    images,
                    randomValue
            );
            final User user = userRepository.findById(dto.sellerId())
                                            .orElseThrow(() -> new UserNotFoundException("지정한 판매자를 찾을 수 없습니다."));
            final Category subCategory = categoryRepository.findSubCategoryById(dto.subCategoryId())
                                                           .orElseThrow(() -> new CategoryNotFoundException(
                                                                   "지정한 하위 카테고리가 없거나 하위 카테고리가 아닙니다."
                                                           ));
            final Auction auction = dto.toEntity(user, subCategory);
            final List<AuctionRegion> auctionRegions = convertAuctionRegions(dto);
            final List<AuctionImage> auctionImages = convertAuctionImages(dto);

            auction.addAuctionRegions(auctionRegions);
            auction.addAuctionImages(auctionImages);

            auctions.add(auction);
            System.out.println("i = " + i);
        }

        auctionRepository.saveAll(auctions);

        return ResponseEntity.noContent().build();
    }

    private List<AuctionRegion> convertAuctionRegions(final CreateAuctionDto dto) {
        final List<AuctionRegion> auctionRegions = new ArrayList<>();

        for (final Long thirdRegionId : dto.thirdRegionIds()) {
            final Region thirdRegion = regionRepository.findThirdRegionById(thirdRegionId)
                                                       .orElseThrow(() -> new RegionNotFoundException(
                                                               "지정한 세 번째 지역이 없거나 세 번째 지역이 아닙니다."
                                                       ));
            auctionRegions.add(new AuctionRegion(thirdRegion));
        }

        return auctionRegions;
    }

    private List<AuctionImage> convertAuctionImages(final CreateAuctionDto dto) {
        return imageProcessor.storeImageFiles(dto.auctionImages())
                             .stream()
                             .map(StoreImageDto::toAuctionImageEntity)
                             .toList();
    }

    @PostMapping("/init/bids")
    public ResponseEntity<Void> bids() {
        Random random = new Random();

        int minValue = 1;
        int maxValue = 30;

        final List<Bid> bids = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            System.out.printf("%d 번째 입찰 저장\n", i);

            long randomAuction = random.nextLong(maxValue - minValue + 1) + minValue;
            final Auction auction = auctionRepository.findAuctionById(randomAuction)
                                                     .orElseThrow(() -> new RegionNotFoundException(
                                                             "지정한 경매가 없습니다."
                                                     ));

            long randomUser;
            do {
                randomUser = random.nextLong(maxValue - minValue + 1) + minValue;
            } while (randomUser == auction.getLastBid().getBidder().getId());

            final User user = userRepository.findById(randomUser)
                                            .orElseThrow(() -> new UserNotFoundException("지정한 사용자를 찾을 수 없습니다."));
            final int bidPrice = convertNextBidPrice(auction, randomAuction);

            final CreateBidDto dto = new CreateBidDto(randomAuction, bidPrice, randomUser);

            final Bid bid = dto.toEntity(auction, user);

            bids.add(bid);
            System.out.println("i = " + i);
        }

        bidRepository.saveAll(bids);

        return ResponseEntity.noContent().build();
    }

    private int convertNextBidPrice(final Auction auction, final long randomAuction) {
        if (auction.getLastBid() != null) {
            return auction.getLastBid().getPrice().getValue() + auction.getBidUnit().getValue();
        }

        return auction.getStartPrice().getValue();
    }

    @PostMapping("/init/auctions/reports")
    public ResponseEntity<Void> auctionReports() {
        Random random = new Random();

        int minValue = 1;
        int maxValue = 30;

        final List<AuctionReport> reports = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            System.out.printf("%d 번째 경매 신고 저장\n", i);

            long randomAuction = random.nextLong(maxValue - minValue + 1) + minValue;
            final Auction auction = auctionRepository.findAuctionById(randomAuction)
                                                     .orElseThrow(() -> new RegionNotFoundException(
                                                             "지정한 경매가 없습니다."
                                                     ));

            long randomUser;
            do {
                randomUser = random.nextLong(maxValue - minValue + 1) + minValue;
            } while (randomUser == auction.getSeller().getId());

            final User user = userRepository.findById(randomUser)
                                            .orElseThrow(() -> new UserNotFoundException("지정한 사용자를 찾을 수 없습니다."));

            final CreateAuctionReportDto dto = new CreateAuctionReportDto(randomAuction, "신고합니다...", randomUser);
            final AuctionReport auctionReport = dto.toEntity(user, auction);

            reports.add(auctionReport);
            System.out.println("i = " + i);
        }

        auctionReportRepository.saveAll(reports);

        return ResponseEntity.noContent().build();
    }
}
