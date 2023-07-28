package com.ddang.ddang.auction.application;

import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionService {

    private final JpaAuctionRepository auctionRepository;
    private final JpaRegionRepository regionRepository;
    private final JpaCategoryRepository categoryRepository;

    @Transactional
    public Long create(final CreateAuctionDto dto) {
        final Auction auction = convertAuction(dto);
        final List<AuctionRegion> auctionRegions = convertToAuctionRegions(dto);

        auction.addAuctionRegions(auctionRegions);
        return auctionRepository.save(auction)
                                .getId();
    }

    private Auction convertAuction(final CreateAuctionDto dto) {
        final Category subCategory = categoryRepository.findSubCategoryById(dto.subCategoryId())
                                                    .orElseThrow(() -> new CategoryNotFoundException(
                                                            "지정한 하위 카테고리가 없거나 하위 카테고리가 아닙니다."
                                                    ));

        return dto.toEntity(subCategory);
    }

    private List<AuctionRegion> convertToAuctionRegions(final CreateAuctionDto dto) {
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
        final Auction auction = auctionRepository.findAuctionWithRegionsById(auctionId)
                                                 .orElseThrow(() -> new AuctionNotFoundException(
                                                         "지정한 아이디에 대한 경매를 찾을 수 없습니다."
                                                 ));

        return ReadAuctionDto.from(auction);
    }

    public List<ReadAuctionDto> readAllByLastAuctionId(final Long lastAuctionId, final int size) {
        final List<Auction> auctions = auctionRepository.findAuctionsAllByLastAuctionId(lastAuctionId, size);

        return auctions.stream()
                       .map(ReadAuctionDto::from)
                       .toList();
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
