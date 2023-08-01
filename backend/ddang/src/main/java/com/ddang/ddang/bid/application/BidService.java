package com.ddang.ddang.bid.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.exception.InvalidPriceValueException;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.bid.application.exception.InvalidAuctionToBidException;
import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import com.ddang.ddang.bid.application.exception.InvalidBidderException;
import com.ddang.ddang.bid.application.exception.UserNotFoundException;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BidService {

    private final JpaAuctionRepository auctionRepository;
    private final JpaUserRepository userRepository;
    private final JpaBidRepository bidRepository;

    @Transactional
    public Long create(final LoginUserDto userDto, final CreateBidDto bidDto) {
        final Auction auction = auctionRepository.findById(bidDto.auctionId())
                                                 .orElseThrow(() -> new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));
        checkInvalidAuction(auction);

        // TODO: 2023/07/28 추후 User 패키지 내에 UserNotFoundException이 생긴다면 해당 예외를 사용하도록 수정 하겠습니다.
        final User bidder = userRepository.findById(userDto.usedId())
                                          .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        checkInvalidBid(auction, bidder, bidDto);

        final Bid saveBid = saveBid(bidDto, auction, bidder);
        return saveBid.getId();
    }

    private Bid saveBid(final CreateBidDto bidDto, final Auction auction, final User bidder) {
        final Bid createBid = bidDto.toEntity(auction, bidder);
        final Bid saveBid = bidRepository.save(createBid);

        auction.updateLastBidPrice(saveBid);

        return saveBid;
    }

    private void checkInvalidAuction(final Auction auction) {
        final LocalDateTime now = LocalDateTime.now();
        if (auction.isClosed(now)) {
            throw new InvalidAuctionToBidException("이미 종료된 경매입니다");
        }
        if (auction.isDeleted()) {
            throw new InvalidAuctionToBidException("삭제된 경매입니다");
        }
    }

    private void checkInvalidBid(final Auction auction, final User bidder, final CreateBidDto bidDto) {
        final Bid lastBid = bidRepository.findLastBidByAuctionId(bidDto.auctionId());
        final Price price = findPrice(bidDto.price());

        if (lastBid == null) {
            checkInvalidFirstBidPrice(auction, price);
            return;
        }

        checkIsNotLastBidder(lastBid, bidder);
        checkInvalidBidPrice(lastBid, price);
    }

    private void checkInvalidFirstBidPrice(final Auction auction, final Price price) {
        if (auction.isInvalidFirstBidPrice(price)) {
            throw new InvalidBidPriceException("입찰 금액이 잘못되었습니다");
        }
    }

    private void checkIsNotLastBidder(final Bid lastBid, final User bidder) {
        // TODO: 2023/07/30 경매 등록자가 입찰하는 경우에 대한 예외 케이스 추가 예정
        if (lastBid.isSameBidder(bidder)) {
            throw new InvalidBidderException("이미 최고 입찰자입니다");
        }
    }

    private void checkInvalidBidPrice(final Bid lastBid, final Price price) {
        if (lastBid.isBidPriceGreaterThan(price)) {
            throw new InvalidBidPriceException("마지막 입찰 금액보다 낮은 금액을 입력했습니다");
        }
        if (lastBid.isNextBidPriceGreaterThan(price)) {
            throw new InvalidBidPriceException("입찰 금액이 잘못되었습니다");
        }
    }

    private Price findPrice(final int value) {
        try {
            return new Price(value);
        } catch (final InvalidPriceValueException ex) {
            throw new InvalidBidPriceException("입찰 금액이 잘못되었습니다");
        }
    }

    public List<ReadBidDto> readAllByAuctionId(final Long auctionId) {
        auctionRepository.findById(auctionId)
                         .orElseThrow(() -> new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));

        final List<Bid> bids = bidRepository.findByAuctionId(auctionId);

        return bids.stream()
                   .map(ReadBidDto::from)
                   .toList();
    }
}
