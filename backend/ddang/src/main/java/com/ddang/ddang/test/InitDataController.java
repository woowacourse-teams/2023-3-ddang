package com.ddang.ddang.test;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
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
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
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
import com.ddang.ddang.report.application.dto.CreateChatRoomReportDto;
import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaAuctionReportRepository;
import com.ddang.ddang.report.infrastructure.persistence.JpaChatRoomReportRepository;
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
import java.time.LocalDateTime;
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
    private final JpaChatRoomReportRepository chatRoomReportRepository;
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
        final List<User> users = new ArrayList<>();

        Random random = new Random();
        double minValue = 0.0d;
        double maxValue = 5.0d;

        for (int i = 0; i < 120; i++) {
            final double randomValue = minValue + (maxValue - minValue) * random.nextDouble();
            final String uuid = UUID.randomUUID().toString();

            final User user = User.builder()
                                  .name("user " + uuid)
                                  .profileImage(null)
                                  .reliability(randomValue)
                                  .oauthId(uuid)
                                  .build();

            users.add(user);

            if (i % 2 == 0) {
                user.update(user.getName(), convertProfileImage(image));
            }
            if (i < 21) {
                user.withdrawal();
            }
        }

        userRepository.saveAll(users);

        return ResponseEntity.noContent().build();
    }

    private ProfileImage convertProfileImage(final MultipartFile image) {
        return imageProcessor.storeImageFile(image).toProfileImageEntity();
    }

    @PostMapping("/init/auctions")
    public ResponseEntity<Void> auctions(
            @RequestPart final List<MultipartFile> images
    ) {
        Random random = new Random();
        LocalDateTime localDateTime = LocalDateTime.now();

        int minValue = 1;
        int maxValue = 120;

        final List<Auction> auctions = new ArrayList<>();

        for (int i = 0; i < 250; i++) {
            System.out.printf("%d 번째 경매 저장\n", i);

            long sellerId = random.nextLong(maxValue - minValue + 1) + minValue;

            final String title = UUID.randomUUID().toString().substring(0, 30);
            final String description = title;

            // 삭제
            LocalDateTime closingTime = localDateTime;
            // 51번 ~ 150번 :
            if (51 <= i && i <= 150) {
                long randomDay = random.nextInt(10) + 1;
                closingTime = localDateTime.minusDays(randomDay);
            }
            // 151 ~ 200
            if (151 <= i && i <= 200) {
                long randomDay = random.nextInt(10) + 50;
                closingTime = localDateTime.plusDays(randomDay);
            }

            final CreateAuctionRequest request = new CreateAuctionRequest(title, description, 100, 10_000, closingTime, 19L, List.of(3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L));

            final CreateAuctionDto dto = CreateAuctionDto.of(
                    request,
                    images,
                    sellerId
            );
            final User user = userRepository.findById(dto.sellerId())
                                            .orElseThrow(() -> new UserNotFoundException("지정한 판매자를 찾을 수 없습니다."));
            final Category subCategory = categoryRepository.findSubCategoryById(dto.subCategoryId())
                                                           .orElseThrow(() -> new CategoryNotFoundException(
                                                                   "지정한 하위 카테고리가 없거나 하위 카테고리가 아닙니다."
                                                           ));
            final Auction auction = dto.toEntity(user, subCategory);
            final List<AuctionRegion> auctionRegions = convertAuctionRegions(dto);
            auction.addAuctionRegions(auctionRegions);

            final List<AuctionImage> auctionImages = convertAuctionImages(dto);
            auction.addAuctionImages(auctionImages);

            if (i < 51) {
                auction.delete();
            }

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
        List<AuctionImage> auctionImages = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            final List<AuctionImage> image = imageProcessor.storeImageFiles(dto.auctionImages())
                                                           .stream()
                                                           .map(StoreImageDto::toAuctionImageEntity)
                                                           .toList();
            auctionImages.addAll(image);
        }

        return auctionImages;
    }

    @PostMapping("/init/bids")
    public ResponseEntity<Void> bids() {
        Random random = new Random();

        int minValue = 52;
        int maxValue = 250;

        final List<Bid> bids = new ArrayList<>();

        for (int i = 0; i < 400; i++) {
            System.out.printf("%d 번째 입찰 저장\n", i);

            long randomAuction = random.nextLong(maxValue - minValue + 1) + minValue;
            System.out.println(randomAuction);
            final Auction auction = auctionRepository.findAuctionById(randomAuction)
                                                     .orElseThrow(() -> new AuctionNotFoundException(
                                                             "지정한 경매가 없습니다."
                                                     ));

            long randomUser;
            do {
                randomUser = random.nextLong(119 - 51 + 1) + minValue;
            } while ((auction.getLastBid() != null && randomUser == auction.getLastBid().getBidder()
                                                                           .getId()) || randomUser == auction.getSeller()
                                                                                                             .getId());

            System.out.println(randomUser);
            final User user = userRepository.findById(randomUser)
                                            .orElseThrow(() -> new UserNotFoundException("지정한 사용자를 찾을 수 없습니다."));
            final int bidPrice = convertNextBidPrice(auction, randomAuction);

            final CreateBidDto dto = new CreateBidDto(randomAuction, bidPrice, randomUser);

            final Bid bid = dto.toEntity(auction, user);
            auction.updateLastBid(bid);

            bids.add(bid);
            System.out.println("i = " + i);
        }

        bidRepository.saveAll(bids);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/init/bids/250")
    public ResponseEntity<Void> bidsTarget() {
        Random random = new Random();

        int minValue = 51;
        int maxValue = 250;

        final List<Bid> bids = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            System.out.printf("%d 번째 입찰 저장\n", i);

            final Auction auction = auctionRepository.findAuctionById(250L)
                                                     .orElseThrow(() -> new AuctionNotFoundException(
                                                             "지정한 경매가 없습니다."
                                                     ));

            long randomUser;
            do {
                randomUser = random.nextLong(119 - 51 + 1) + minValue;
            } while ((auction.getLastBid() != null && randomUser == auction.getLastBid().getBidder()
                                                                           .getId()) || randomUser == auction.getSeller()
                                                                                                             .getId());

            final User user = userRepository.findById(randomUser)
                                            .orElseThrow(() -> new UserNotFoundException("지정한 사용자를 찾을 수 없습니다."));
            final int bidPrice = convertNextBidPrice(auction, 250L);

            final CreateBidDto dto = new CreateBidDto(250L, bidPrice, randomUser);

            final Bid bid = dto.toEntity(auction, user);
            auction.updateLastBid(bid);

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

        int minValue = 52;
        int maxValue = 250;

        final List<AuctionReport> reports = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            System.out.printf("%d 번째 경매 신고 저장\n", i);

            long randomAuction = random.nextLong(maxValue - minValue + 1) + minValue;
            final Auction auction = auctionRepository.findAuctionById(randomAuction)
                                                     .orElseThrow(() -> new AuctionNotFoundException(
                                                             "지정한 경매가 없습니다."
                                                     ));

            long randomUser;
            do {
                randomUser = random.nextLong(119 - 51 + 1) + minValue;
            } while (randomUser == auction.getSeller().getId());

            final User user = userRepository.findById(randomUser)
                                            .orElseThrow(() -> new UserNotFoundException("지정한 사용자를 찾을 수 없습니다."));

            final String description = UUID.randomUUID().toString().substring(0, 30);
            final CreateAuctionReportDto dto = new CreateAuctionReportDto(randomAuction, description, randomUser);
            final AuctionReport auctionReport = dto.toEntity(user, auction);

            reports.add(auctionReport);
            System.out.println("i = " + i);
        }

        auctionReportRepository.saveAll(reports);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/init/chat-room/reports")
    public ResponseEntity<Void> chatRoomReports() {
        final List<ChatRoomReport> reports = new ArrayList<>();

        for (long chatRoomId = 52; chatRoomId < 102; chatRoomId++) {
            System.out.printf("%d 번째 채팅룸 신고 저장\n", chatRoomId);

            final ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId)
                                                        .orElseThrow(() -> new ChatRoomNotFoundException(
                                                                "지정한 경매가 없습니다."
                                                        ));

            long userId = chatRoom.getBuyer().getId();
            final User user = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("지정한 사용자를 찾을 수 없습니다."));

            final String description = UUID.randomUUID().toString().substring(0, 30);
            final CreateChatRoomReportDto dto = new CreateChatRoomReportDto(chatRoomId, description, userId);
            final ChatRoomReport chatRoomReport = dto.toEntity(user, chatRoom);

            reports.add(chatRoomReport);
            System.out.println("chatRoomId = " + chatRoomId);
        }

        chatRoomReportRepository.saveAll(reports);

        return ResponseEntity.noContent().build();
    }
}
