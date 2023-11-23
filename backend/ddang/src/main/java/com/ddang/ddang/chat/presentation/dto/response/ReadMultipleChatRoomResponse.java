package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReadMultipleChatRoomResponse(
        Long id,
        ReadPartnerResponse partner,
        ReadChatRoomAuctionInfoResponse auction,
        ReadLastMessageResponse lastMessage,
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
                ReadPartnerResponse.of(dto, profileImageRelativeUrl),
                ReadChatRoomAuctionInfoResponse.of(dto, auctionImageRelativeUrl),
                new ReadLastMessageResponse(dto.lastMessageDto().createAd(), dto.lastMessageDto().content()),
                dto.unreadMessageCount(),
                dto.isChatAvailable()
        );
    }

    public record ReadPartnerResponse(Long id, String name, String profileImage) {

        private static ReadPartnerResponse of(final ReadMultipleChatRoomDto dto, final String imageRelativeUrl) {
            return new ReadPartnerResponse(
                    dto.partnerDto().id(),
                    dto.partnerDto().name(),
                    imageRelativeUrl + dto.partnerDto().profileImageStoreName()
            );
        }
    }

    public record ReadChatRoomAuctionInfoResponse(Long id, String title, String image, int price) {

        private static ReadChatRoomAuctionInfoResponse of(final ReadMultipleChatRoomDto dto, final String imageRelativeUrl) {
            return new ReadChatRoomAuctionInfoResponse(
                    dto.auctionDto().id(),
                    dto.auctionDto().title(),
                    imageRelativeUrl + dto.auctionDto().thumbnailImageStoreName(),
                    dto.auctionDto().lastBidPrice()
            );
        }
    }

    public record ReadLastMessageResponse(
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdTime,

            String content
    ) {
    }
}
