package com.ddang.ddang.auction.application;

import com.ddang.ddang.auction.application.dto.request.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.response.CreateInfoAuctionDto;
import com.ddang.ddang.auction.application.dto.response.ReadSingleAuctionDto;
import com.ddang.ddang.auction.application.dto.response.ReadMultipleAuctionDto;
import com.ddang.ddang.auction.application.exception.UserForbiddenException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.domain.repository.CategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.domain.repository.RegionRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final RegionRepository regionRepository;
    private final CategoryRepository categoryRepository;
    private final StoreImageProcessor imageProcessor;

    @Transactional
    public CreateInfoAuctionDto create(final CreateAuctionDto dto) {
        final User seller = userRepository.getByIdOrThrow(dto.sellerId());
        final Category subCategory = categoryRepository.getSubCategoryByIdOrThrow(dto.subCategoryId());
        final Auction auction = dto.toEntity(seller, subCategory);
        final List<Region> thirdRegions = regionRepository.findAllThirdRegionByIds(dto.thirdRegionIds());

        validateAuctionRegions(thirdRegions);

        final List<AuctionRegion> auctionRegions = thirdRegions.stream()
                                                               .map(AuctionRegion::new)
                                                               .toList();
        final List<AuctionImage> auctionImages = imageProcessor.storeImageFiles(dto.auctionImages())
                                                               .stream()
                                                               .map(StoreImageDto::toAuctionImageEntity)
                                                               .toList();

        auction.addAuctionRegions(auctionRegions);
        auction.addAuctionImages(auctionImages);

        final Auction persistAuction = auctionRepository.save(auction);

        return CreateInfoAuctionDto.from(persistAuction);
    }

    private void validateAuctionRegions(final List<Region> thirdRegions) {
        if (thirdRegions.isEmpty()) {
            throw new RegionNotFoundException("지정한 세 번째 지역이 없습니다.");
        }
    }

    public ReadSingleAuctionDto readByAuctionId(final Long auctionId) {
        final Auction findAuction = auctionRepository.getTotalAuctionByIdOrThrow(auctionId);

        return ReadSingleAuctionDto.of(findAuction, LocalDateTime.now());
    }

    public ReadMultipleAuctionDto readAllByCondition(
            final Pageable pageable,
            final ReadAuctionSearchCondition readAuctionSearchCondition) {
        final Slice<Auction> auctions = auctionRepository.findAuctionsAllByCondition(
                readAuctionSearchCondition,
                pageable
        );

        return ReadMultipleAuctionDto.of(auctions, LocalDateTime.now());
    }

    public ReadMultipleAuctionDto readAllByUserId(final Long userId, final Pageable pageable) {
        final Slice<Auction> auctions = auctionRepository.findAuctionsAllByUserId(userId, pageable);

        return ReadMultipleAuctionDto.of(auctions, LocalDateTime.now());
    }

    public ReadMultipleAuctionDto readAllByBidderId(final Long userId, final Pageable pageable) {
        final Slice<Auction> auctions = auctionRepository.findAuctionsAllByBidderId(userId, pageable);

        return ReadMultipleAuctionDto.of(auctions, LocalDateTime.now());
    }

    @Transactional
    public void deleteByAuctionId(final Long auctionId, final Long userId) {
        final Auction findAuction = auctionRepository.getTotalAuctionByIdOrThrow(auctionId);
        final User user = userRepository.getByIdOrThrow(userId);

        if (!findAuction.isOwner(user)) {
            throw new UserForbiddenException("권한이 없습니다.");
        }

        findAuction.delete();
    }
}
