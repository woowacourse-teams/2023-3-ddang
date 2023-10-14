package com.ddang.ddang.chat.domain.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.image.domain.AuctionImage;

public record ChatRoomAndImageDto(ChatRoom chatRoom, AuctionImage thumbnailImage) {
}
