package com.ddang.ddang.test;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final JpaChatRoomRepository chatRoomRepository;
    private final JpaAuctionRepository auctionRepository;
    private final JpaRegionRepository regionRepository;
    private final JpaCategoryRepository categoryRepository;
    private final StoreImageProcessor imageProcessor;

    @PostMapping("/init/users")
    public ResponseEntity<Void> users(
            @RequestPart final MultipartFile image
    ) throws IOException {
        final List<User> users = new ArrayList<>();

        Random random = new Random();
        double minValue = 0.0d;
        double maxValue = 5.0d;

        for (int i = 0; i < 10; i++) {
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
        int maxValue = 10_000;

        final List<Auction> auctions = new ArrayList<>();

        for (int i = 0; i < 20_000; i++) {
            System.out.printf("%d 번째 사용자 저장\n", i);

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
}
