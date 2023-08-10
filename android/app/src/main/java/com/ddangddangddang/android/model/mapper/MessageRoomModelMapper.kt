package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.MessageRoomModel
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MessageRoomModelMapper : Mapper<MessageRoomModel, ChatRoomPreviewResponse> {
    override fun ChatRoomPreviewResponse.toPresentation(): MessageRoomModel {
        return MessageRoomModel(
            id,
            auction.title,
            chatPartner.profileImage,
            chatPartner.name,
            "마지막 메시지 내용", // 수정 필요
            formatLastMessageTime(LocalDateTime.now()), // 수정 필요 LocalDateTimeFormatter.parse 사용할 것
            isChatAvailable,
        )
    }

    private fun formatLastMessageTime(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")
        return dateTime.format(formatter)
    }
}
