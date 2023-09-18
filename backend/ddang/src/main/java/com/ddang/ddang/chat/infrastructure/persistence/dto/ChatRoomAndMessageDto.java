package com.ddang.ddang.chat.infrastructure.persistence.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.image.domain.AuctionImage;

public record ChatRoomAndMessageDto(ChatRoom chatRoom, Message message, AuctionImage thumbnailImage) {
}
