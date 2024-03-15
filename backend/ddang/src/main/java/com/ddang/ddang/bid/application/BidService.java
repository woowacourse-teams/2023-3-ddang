package com.ddang.ddang.bid.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.dto.AuctionAndImageDto;
import com.ddang.ddang.auction.domain.repository.AuctionAndImageRepository;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.application.dto.BidDto;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.bid.application.event.BidNotificationEvent;
import com.ddang.ddang.bid.application.exception.InvalidAuctionToBidException;
import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import com.ddang.ddang.bid.application.exception.InvalidBidderException;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.domain.repository.BidRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BidService {

    private final ApplicationEventPublisher bidEventPublisher;
    private final AuctionRepository auctionRepository;
    private final AuctionAndImageRepository auctionAndImageRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final JpaAuctionRepository jpaAuctionRepository;

    @Transactional
    public Long create(final CreateBidDto bidDto, final String auctionImageAbsoluteUrl) {
        System.out.println("createBidDto: " + bidDto);

        final User bidder = userRepository.findById(bidDto.userId())
                                          .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        final AuctionAndImageDto auctionAndImageDto =
                auctionAndImageRepository.findDtoByAuctionIdWithLock(bidDto.auctionId())
                                         .orElseThrow(() -> new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));
        final Auction auction = auctionAndImageDto.auction();
        final List<Bid> bids = bidRepository.findAllByAuctionId(auction.getId());

        System.out.println("find auction: " + auction);
        System.out.println("find lastBid: " + auction.getLastBid());
        System.out.println("find bids: " + bids);
        if (bids.size() > 0) {
            System.out.println("find bids auction: " + bids.get(0).getAuction());
            System.out.println("find bids auction lastibd: " + bids.get(0).getAuction().getLastBid());
        }

        checkInvalidAuction(auction);
        checkInvalidBid(auction, bidder, bidDto);

//        auction.findLastBidder()
//               .ifPresent(previousBidder ->
//                       publishBidNotificationEvent(auctionImageAbsoluteUrl, auctionAndImageDto, previousBidder));

        return saveAndUpdateLastBid(bidDto, auction, bidder).getId();
    }

    private void publishBidNotificationEvent(
            final String auctionImageAbsoluteUrl,
            final AuctionAndImageDto auctionAndImageDto,
            final User previousBidder
    ) {
        final BidDto bidDto = new BidDto(
                previousBidder.getId(),
                auctionAndImageDto,
                auctionImageAbsoluteUrl
        );

        bidEventPublisher.publishEvent(new BidNotificationEvent(bidDto));
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
        checkIsSeller(auction, bidder);

        final BidPrice bidPrice = processBidPrice(bidDto.bidPrice());

        auction.findLastBid()
               .ifPresentOrElse(
                       lastBid -> {
                           checkIsNotLastBidder(lastBid, bidder);
                           checkInvalidBidPrice(lastBid, bidPrice);
                       },
                       () -> checkInvalidFirstBidPrice(auction, bidPrice)
               );
    }

    private BidPrice processBidPrice(final int value) {
        try {
            return new BidPrice(value);
        } catch (final InvalidBidPriceException ex) {
            throw new InvalidBidPriceException("입찰 금액이 잘못되었습니다");
        }
    }

    private void checkIsSeller(final Auction auction, final User bidder) {
        if (auction.isOwner(bidder)) {
            throw new InvalidBidderException("판매자는 입찰할 수 없습니다");
        }
    }

    private void checkInvalidFirstBidPrice(final Auction auction, final BidPrice bidPrice) {
        if (auction.isInvalidFirstBidPrice(bidPrice)) {
            throw new InvalidBidPriceException("입찰 금액이 잘못되었습니다");
        }
    }

    private void checkIsNotLastBidder(final Bid lastBid, final User bidder) {
        if (lastBid.isSameBidder(bidder)) {
            throw new InvalidBidderException("이미 최고 입찰자입니다");
        }
    }

    private void checkInvalidBidPrice(final Bid lastBid, final BidPrice bidPrice) {
        if (lastBid.isNextBidPriceGreaterThan(bidPrice)) {
            throw new InvalidBidPriceException("가능 입찰액보다 낮은 금액을 입력했습니다");
        }
    }

    private Bid saveAndUpdateLastBid(final CreateBidDto bidDto, final Auction auction, final User bidder) {
        final Bid createBid = bidDto.toEntity(auction, bidder);
        final Bid saveBid = bidRepository.save(createBid);

        auction.updateLastBid(saveBid);

        System.out.println("update auction: " + auction);
        System.out.println("update auction lastBid: " + auction.getLastBid());

        return saveBid;
    }

    public List<ReadBidDto> readAllByAuctionId(final Long auctionId) {
        if (auctionRepository.existsById(auctionId)) {
            final List<Bid> bids = bidRepository.findAllByAuctionId(auctionId);

            return bids.stream()
                       .map(ReadBidDto::from)
                       .toList();
        }

        throw new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");
    }
}
