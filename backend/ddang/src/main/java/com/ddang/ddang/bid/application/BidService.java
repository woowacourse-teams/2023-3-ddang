package com.ddang.ddang.bid.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.dto.AuctionAndImageDto;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.bid.application.exception.InvalidAuctionToBidException;
import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import com.ddang.ddang.bid.application.exception.InvalidBidderException;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.notification.application.NotificationService;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationType;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BidService {

    private static final String BID_NOTIFICATION_MESSAGE_FORMAT = "상위 입찰자가 나타났습니다. 구매를 원하신다면 더 높은 가격을 제시해 주세요.";

    private final NotificationService notificationService;
    private final JpaAuctionRepository auctionRepository;
    private final JpaUserRepository userRepository;
    private final JpaBidRepository bidRepository;

    @Transactional
    public Long create(final CreateBidDto bidDto, final String absoluteUrl) {
        final User bidder = userRepository.findById(bidDto.userId())
                                          .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        final AuctionAndImageDto auctionAndImageDto =
                auctionRepository.findDtoByAuctionId(bidDto.auctionId())
                                 .orElseThrow(() -> new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));

        final Auction auction = auctionAndImageDto.auction();
        checkInvalidAuction(auction);
        checkInvalidBid(auction, bidder, bidDto);

        final Optional<User> previousBidder = auction.findLastBidder();

        final Bid saveBid = saveBid(bidDto, auction, bidder);

        if (previousBidder.isEmpty()) {
            return saveBid.getId();
        }

        try {
            final String sendNotificationMessage =
                    sendNotification(auctionAndImageDto, previousBidder.get(), absoluteUrl);
            log.info(sendNotificationMessage);
        } catch (Exception ex) {
            log.error("exception type : {}, ", ex.getClass().getSimpleName(), ex);
        }

        return saveBid.getId();
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
        final BidPrice bidPrice = processBidPrice(bidDto.bidPrice());

        checkIsSeller(auction, bidder);

        if (lastBid == null) {
            checkInvalidFirstBidPrice(auction, bidPrice);
            return;
        }

        checkIsNotLastBidder(lastBid, bidder);
        checkInvalidBidPrice(lastBid, bidPrice);
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

    private Bid saveBid(final CreateBidDto bidDto, final Auction auction, final User bidder) {
        final Bid createBid = bidDto.toEntity(auction, bidder);
        final Bid saveBid = bidRepository.save(createBid);

        auction.updateLastBid(saveBid);

        return saveBid;
    }

    private String sendNotification(
            final AuctionAndImageDto auctionAndImageDto,
            final User previousBidder,
            final String absoluteUrl
    ) {
        final Auction auction = auctionAndImageDto.auction();
        final AuctionImage auctionImage = auctionAndImageDto.auctionImage();
        final CreateNotificationDto dto = new CreateNotificationDto(
                NotificationType.BID,
                previousBidder.getId(),
                auction.getTitle(),
                BID_NOTIFICATION_MESSAGE_FORMAT,
                calculateRedirectUrl(auction.getId()),
                ImageUrlCalculator.calculateAuctionImageUrl(auctionImage, absoluteUrl)
        );

        return notificationService.send(dto);
    }

    private String calculateRedirectUrl(final Long auctionId) {
        return "/auctions/" + auctionId;
    }

    public List<ReadBidDto> readAllByAuctionId(final Long auctionId) {
        if (auctionRepository.existsById(auctionId)) {
            final List<Bid> bids = bidRepository.findByAuctionIdOrderByIdAsc(auctionId);

            return bids.stream()
                       .map(ReadBidDto::from)
                       .toList();
        }

        throw new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");
    }
}
