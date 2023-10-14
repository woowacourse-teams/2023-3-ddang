package com.ddang.ddang.bid.infrastructure.persistence;

import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.repository.BidRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BidRepositoryImpl implements BidRepository {

    private final JpaBidRepository jpaBidRepository;

    @Override
    public Bid save(final Bid bid) {
        return jpaBidRepository.save(bid);
    }

    @Override
    public List<Bid> findAllByAuctionId(final Long auctionId) {
        return jpaBidRepository.findAllByAuctionIdOrderByIdAsc(auctionId);
    }

    @Override
    public Bid findLastBidByAuctionId(final Long auctionId) {
        return jpaBidRepository.findLastBidByAuctionId(auctionId);
    }
}
