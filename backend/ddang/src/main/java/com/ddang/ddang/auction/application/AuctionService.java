package com.ddang.ddang.auction.application;

import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.application.exception.UserNotAuthorizationException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionService {

    private final JpaUserRepository userRepository;
    private final JpaAuctionRepository auctionRepository;
    private final JpaRegionRepository regionRepository;
    private final JpaCategoryRepository categoryRepository;
    private final StoreImageProcessor imageProcessor;

    @Transactional
    public CreateInfoAuctionDto create(final CreateAuctionDto dto) {
        final User seller = findSeller(dto);
        final Category subCategory = findSubCategory(dto);
        final Auction auction = dto.toEntity(seller, subCategory);
        final List<AuctionRegion> auctionRegions = convertAuctionRegions(dto);
        final List<AuctionImage> auctionImages = convertAuctionImages(dto);

        auction.addAuctionRegions(auctionRegions);
        auction.addAuctionImages(auctionImages);

        final Auction persistAuction = auctionRepository.save(auction);

        return CreateInfoAuctionDto.from(persistAuction);
    }

    private User findSeller(final CreateAuctionDto dto) {
        return userRepository.findById(dto.sellerId())
                             .orElseThrow(() -> new UserNotFoundException("지정한 판매자를 찾을 수 없습니다."));
    }

    private Category findSubCategory(final CreateAuctionDto dto) {
        return categoryRepository.findSubCategoryById(dto.subCategoryId())
                                 .orElseThrow(() -> new CategoryNotFoundException(
                                         "지정한 하위 카테고리가 없거나 하위 카테고리가 아닙니다."
                                 ));
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
                             .map(StoreImageDto::toEntity)
                             .toList();
    }

    public ReadAuctionDto readByAuctionId(final Long auctionId) {
        final Auction auction = findAuction(auctionId);

        return ReadAuctionDto.from(auction);
    }

    private Auction findAuction(final Long auctionId) {
        return auctionRepository.findAuctionById(auctionId)
                                .orElseThrow(() -> new AuctionNotFoundException(
                                        "지정한 아이디에 대한 경매를 찾을 수 없습니다."
                                ));
    }

    public ReadAuctionsDto readAllByLastAuctionId(final Long lastAuctionId, final int size) {
        final Slice<Auction> auctions = auctionRepository.findAuctionsAllByLastAuctionId(lastAuctionId, size);

        return ReadAuctionsDto.from(auctions);
    }

    @Transactional
    public void deleteByAuctionId(final Long auctionId, final Long userId) {
        final Auction auction = findAuction(auctionId);
        final User user = findUser(userId);

        if (!auction.isOwner(user)) {
            throw new UserNotAuthorizationException("권한이 없습니다.");
        }

        auction.delete();
    }

    private User findUser(final Long userId) {
        return userRepository.findById(userId)
                                        .orElseThrow(() -> new UserNotFoundException("회원 정보를 찾을 수 없습니다."));
    }
}
