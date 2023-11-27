package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReadMultipleChatRoomResponse(
        Long id,
        PartnerResponse partner,
        SimpleAuctionInfoResponse auction,
        LastMessageInfoResponse lastMessage,
        Long unreadMessageCount,
        boolean isChatAvailable
) {

    public static ReadMultipleChatRoomResponse of(
            final ReadMultipleChatRoomDto dto,
            final String profileImageRelativeUrl,
            final String auctionImageRelativeUrl
    ) {
        return new ReadMultipleChatRoomResponse(
                dto.chatRoomId(),
                PartnerResponse.of(dto, profileImageRelativeUrl),
                SimpleAuctionInfoResponse.of(dto, auctionImageRelativeUrl),
                new LastMessageInfoResponse(dto.lastMessageDto().createdTime(), dto.lastMessageDto().content()),
                dto.unreadMessageCount(),
                dto.isChatAvailable()
        );
    }

    public record PartnerResponse(Long id, String name, String profileImage) {

        public static PartnerResponse of(final ReadMultipleChatRoomDto dto, final String imageRelativeUrl) {
            return new PartnerResponse(
                    dto.partnerDto().id(),
                    dto.partnerDto().name(),
                    imageRelativeUrl + dto.partnerDto().profileImageStoreName()
            );
        }
    }

    public record SimpleAuctionInfoResponse(Long id, String title, String image, int price) {

        private static SimpleAuctionInfoResponse of(final ReadMultipleChatRoomDto dto, final String imageRelativeUrl) {
            return new SimpleAuctionInfoResponse(
                    dto.auctionDto().id(),
                    dto.auctionDto().title(),
                    imageRelativeUrl + dto.auctionDto().thumbnailImageStoreName(),
                    dto.auctionDto().lastBidPrice()
            );
        }
    }

    public record LastMessageInfoResponse(
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdTime,

            String content
    ) {
    }
}
