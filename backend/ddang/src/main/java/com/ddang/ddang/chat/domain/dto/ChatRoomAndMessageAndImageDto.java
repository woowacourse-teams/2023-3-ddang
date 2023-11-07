package com.ddang.ddang.chat.domain.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.image.domain.AuctionImage;

public record ChatRoomAndMessageAndImageDto(
        ChatRoom chatRoom,
        Message message,
        AuctionImage thumbnailImage,
        Long unreadMessageCount
) {
}
