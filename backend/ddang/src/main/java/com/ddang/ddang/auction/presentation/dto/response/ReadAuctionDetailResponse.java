package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.response.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.user.presentation.util.ReliabilityProcessor;

public record ReadAuctionDetailResponse(
        AuctionDetailResponse auction,
        SellerResponse seller,
        ChatRoomInAuctionResponse chat,
        boolean isOwner,
        boolean isLastBidder
) {

    public static ReadAuctionDetailResponse of(
            final ReadAuctionDto auctionDto,
            final AuthenticationUserInfo userInfo,
            final ReadChatRoomDto chatRoomDto,
            final String profileImageRelativeUrl,
            final String auctionImageRelativeUrl
    ) {
        final AuctionDetailResponse auctionDetailResponse = AuctionDetailResponse.of(
                auctionDto,
                auctionImageRelativeUrl
        );
        final SellerResponse sellerResponse = SellerResponse.of(auctionDto, profileImageRelativeUrl);
        final ChatRoomInAuctionResponse chatRoomResponse = ChatRoomInAuctionResponse.from(chatRoomDto);

        return new ReadAuctionDetailResponse(
                auctionDetailResponse,
                sellerResponse,
                chatRoomResponse,
                isOwner(auctionDto, userInfo),
                isLastBidder(auctionDto, userInfo)
        );
    }

    private static boolean isOwner(final ReadAuctionDto auctionDto, final AuthenticationUserInfo userInfo) {
        return auctionDto.sellerId().equals(userInfo.userId());
    }

    private static boolean isLastBidder(final ReadAuctionDto auctionDto, final AuthenticationUserInfo userInfo) {
        return userInfo.userId().equals(auctionDto.lastBidderId());
    }

    private record SellerResponse(Long id, String image, String nickname, Float reliability) {

        private static SellerResponse of(final ReadAuctionDto auctionDto, final String imageRelativeUrl) {
            return new SellerResponse(
                    auctionDto.sellerId(),
                    imageRelativeUrl + auctionDto.sellerProfileStoreName(),
                    auctionDto.sellerName(),
                    ReliabilityProcessor.process(auctionDto.sellerReliability())
            );
        }
    }

    private record ChatRoomInAuctionResponse(Long id, boolean isChatParticipant) {

        public static ChatRoomInAuctionResponse from(final ReadChatRoomDto dto) {
            return new ChatRoomInAuctionResponse(dto.id(), dto.isChatParticipant());
        }
    }
}
