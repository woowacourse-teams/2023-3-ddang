package com.ddang.ddang.auction.application;

import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionService {

    private final JpaAuctionRepository auctionRepository;

    @Transactional
    public Long create(final CreateAuctionDto dto) {
        return auctionRepository.save(dto.toEntity())
                                .getId();
    }

    public ReadAuctionDto readByAuctionId(final Long auctionId) {
        final Auction auction = auctionRepository.findById(auctionId)
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
