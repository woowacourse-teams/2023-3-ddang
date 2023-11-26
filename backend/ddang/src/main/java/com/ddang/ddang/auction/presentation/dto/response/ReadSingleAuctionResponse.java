package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.response.ReadFullDirectRegionDto;
import com.ddang.ddang.auction.application.dto.response.ReadSingleAuctionDto;
import com.ddang.ddang.chat.application.dto.response.ReadChatRoomDto;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ReadSingleAuctionResponse(
        ReadAuctionResponse auction,
        SellerResponse seller,
        ChatRoomInAuctionResponse chat,
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
        final ReadAuctionResponse readAuctionResponse = ReadAuctionResponse.of(auctionDto, auctionImageRelativeUrl);
        final SellerResponse sellerResponse = SellerResponse.of(auctionDto, profileImageRelativeUrl);
        final ChatRoomInAuctionResponse chatRoomResponse = ChatRoomInAuctionResponse.from(chatRoomDto);

        return new ReadSingleAuctionResponse(
                readAuctionResponse,
                sellerResponse,
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

    private record SellerResponse(Long id, String image, String nickname, Float reliability) {

        private static SellerResponse of(final ReadSingleAuctionDto auctionDto, final String imageRelativeUrl) {
            return new SellerResponse(
                    auctionDto.sellerId(),
                    imageRelativeUrl + auctionDto.sellerProfileStoreName(),
                    auctionDto.sellerName(),
                    auctionDto.sellerReliability()
            );
        }
    }

    private record ChatRoomInAuctionResponse(Long id, boolean isChatParticipant) {

        public static ChatRoomInAuctionResponse from(final ReadChatRoomDto dto) {
            return new ChatRoomInAuctionResponse(dto.id(), dto.isChatParticipant());
        }
    }

    public record ReadAuctionResponse(
            Long id,

            List<String> images,

            String title,

            ReadCategoryResponse category,

            String description,

            int startPrice,

            Integer lastBidPrice,

            String status,

            int bidUnit,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime registerTime,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime closingTime,

            List<ReadDirectRegionResponse> directRegions,

            int auctioneerCount
    ) {

        public static ReadAuctionResponse of(final ReadSingleAuctionDto dto, final String imageRelativeUrl) {
            return new ReadAuctionResponse(
                    dto.id(),
                    convertImageUrls(dto, imageRelativeUrl),
                    dto.title(),
                    new ReadCategoryResponse(dto.mainCategory(), dto.subCategory()),
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

        private static List<ReadDirectRegionResponse> convertDirectRegionsResponse(final ReadSingleAuctionDto dto) {
            return dto.auctionRegions()
                      .stream()
                      .map(ReadDirectRegionResponse::from)
                      .toList();
        }

        public record ReadCategoryResponse(String main, String sub) {
        }

        public record ReadDirectRegionResponse(String first, String second, String third) {

            private static ReadDirectRegionResponse from(final ReadFullDirectRegionDto dto) {
                return new ReadDirectRegionResponse(
                        dto.firstRegionDto().regionName(),
                        dto.secondRegionDto().regionName(),
                        dto.thirdRegionDto().regionName()
                );
            }
        }
    }
}
