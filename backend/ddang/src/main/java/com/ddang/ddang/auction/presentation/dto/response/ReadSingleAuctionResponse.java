package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.response.ReadFullDirectRegionDto;
import com.ddang.ddang.auction.application.dto.response.ReadSingleAuctionDto;
import com.ddang.ddang.chat.application.dto.response.ReadChatRoomDto;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ReadSingleAuctionResponse(
        AuctionInfoResponse auction,
        SellerInfoResponse seller,
        ChatRoomInfoResponse chat,
        boolean isOwner,
        boolean isLastBidder
) {

    public static ReadSingleAuctionResponse of(
            final ReadSingleAuctionDto auctionDto,
            final AuthenticationUserInfo userInfo,
            final ReadChatRoomDto chatRoomDto,
            final String profileImageRelativeUrl,
            final String auctionImageRelativeUrl
    ) {
        final AuctionInfoResponse auctionInfoResponse = AuctionInfoResponse.of(auctionDto, auctionImageRelativeUrl);
        final SellerInfoResponse sellerInfoResponse = SellerInfoResponse.of(auctionDto, profileImageRelativeUrl);
        final ChatRoomInfoResponse chatRoomResponse = ChatRoomInfoResponse.from(chatRoomDto);

        return new ReadSingleAuctionResponse(
                auctionInfoResponse,
                sellerInfoResponse,
                chatRoomResponse,
                isOwner(auctionDto, userInfo),
                isLastBidder(auctionDto, userInfo)
        );
    }

    private static boolean isOwner(final ReadSingleAuctionDto auctionDto, final AuthenticationUserInfo userInfo) {
        return auctionDto.sellerId().equals(userInfo.userId());
    }

    private static boolean isLastBidder(final ReadSingleAuctionDto auctionDto, final AuthenticationUserInfo userInfo) {
        return userInfo.userId().equals(auctionDto.lastBidderId());
    }

    public record SellerInfoResponse(Long id, String image, String nickname, Float reliability) {

        public static SellerInfoResponse of(final ReadSingleAuctionDto auctionDto, final String imageRelativeUrl) {
            return new SellerInfoResponse(
                    auctionDto.sellerId(),
                    imageRelativeUrl + auctionDto.sellerProfileStoreName(),
                    auctionDto.sellerName(),
                    auctionDto.sellerReliability()
            );
        }
    }

    public record ChatRoomInfoResponse(Long id, boolean isChatParticipant) {

        public static ChatRoomInfoResponse from(final ReadChatRoomDto dto) {
            return new ChatRoomInfoResponse(dto.id(), dto.isChatParticipant());
        }
    }

    public record AuctionInfoResponse(
            Long id,

            List<String> images,

            String title,

            CategoryInfoResponse category,

            String description,

            int startPrice,

            Integer lastBidPrice,

            String status,

            int bidUnit,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime registerTime,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime closingTime,

            List<DirectRegionInfoResponse> directRegions,

            int auctioneerCount
    ) {

        public static AuctionInfoResponse of(final ReadSingleAuctionDto dto, final String imageRelativeUrl) {
            return new AuctionInfoResponse(
                    dto.id(),
                    convertImageUrls(dto, imageRelativeUrl),
                    dto.title(),
                    new CategoryInfoResponse(dto.mainCategory(), dto.subCategory()),
                    dto.description(),
                    dto.startPrice(),
                    dto.lastBidPrice(),
                    dto.auctionStatus().name(),
                    dto.bidUnit(),
                    dto.registerTime(),
                    dto.closingTime(),
                    convertDirectRegionsResponse(dto),
                    dto.auctioneerCount()
            );
        }

        private static List<String> convertImageUrls(final ReadSingleAuctionDto dto, final String imageRelativeUrl) {
            return dto.auctionImageStoreNames()
                      .stream()
                      .map(storeName -> imageRelativeUrl + storeName)
                      .toList();
        }

        private static List<DirectRegionInfoResponse> convertDirectRegionsResponse(final ReadSingleAuctionDto dto) {
            return dto.auctionRegions()
                      .stream()
                      .map(DirectRegionInfoResponse::from)
                      .toList();
        }

        public record CategoryInfoResponse(String main, String sub) {
        }

        public record DirectRegionInfoResponse(String first, String second, String third) {

            private static DirectRegionInfoResponse from(final ReadFullDirectRegionDto dto) {
                return new DirectRegionInfoResponse(
                        dto.firstRegionDto().regionName(),
                        dto.secondRegionDto().regionName(),
                        dto.thirdRegionDto().regionName()
                );
            }
        }
    }
}
