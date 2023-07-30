package com.ddang.ddang.auction.application;

import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionService {

    private final JpaAuctionRepository auctionRepository;
    private final JpaRegionRepository regionRepository;
    private final JpaCategoryRepository categoryRepository;
    private final StoreImageProcessor imageProcessor;

    @Transactional
    public CreateInfoAuctionDto create(final CreateAuctionDto dto) {
        final Auction auction = convertAuction(dto);
        final List<AuctionRegion> auctionRegions = convertAuctionRegions(dto);
        final List<AuctionImage> auctionImages = convertAuctionImage(dto);

        auction.addAuctionRegions(auctionRegions);
        auction.addAuctionImages(auctionImages);

        final Auction persistAuction = auctionRepository.save(auction);

        return CreateInfoAuctionDto.from(persistAuction);
    }

    private List<AuctionImage> convertAuctionImage(final CreateAuctionDto dto) {
        return imageProcessor.storeImageFiles(dto.auctionImages())
                             .stream()
                             .map(imageDto -> new AuctionImage(imageDto.uploadName(), imageDto.storeName()))
                             .toList();
    }

    private Auction convertAuction(final CreateAuctionDto dto) {
        final Category subCategory = categoryRepository.findSubCategoryById(dto.subCategoryId())
                                                       .orElseThrow(() -> new CategoryNotFoundException(
                                                               "지정한 하위 카테고리가 없거나 하위 카테고리가 아닙니다."
                                                       ));

        return dto.toEntity(subCategory);
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

    public ReadAuctionDto readByAuctionId(final Long auctionId) {
        final Auction auction = auctionRepository.findAuctionById(auctionId)
                                                 .orElseThrow(() -> new AuctionNotFoundException(
                                                         "지정한 아이디에 대한 경매를 찾을 수 없습니다."
                                                 ));

        return ReadAuctionDto.from(auction);
    }

    public ReadAuctionsDto readAllByLastAuctionId(final Long lastAuctionId, final int size) {
        final Slice<Auction> auctions = auctionRepository.findAuctionsAllByLastAuctionId(lastAuctionId, size);

        return ReadAuctionsDto.from(auctions);
    }

    @Transactional
    public void deleteByAuctionId(final Long auctionId) {
        final Auction auction = auctionRepository.findById(auctionId)
                                                 .orElseThrow(() -> new AuctionNotFoundException(
                                                         "지정한 아이디에 대한 경매를 찾을 수 없습니다."
                                                 ));

        auction.delete();
    }
}
