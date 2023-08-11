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
            chatPartner.profileImage ?: "",
            chatPartner.name,
            lastMessage?.contents ?: "", // 수정 필요
            formatLastMessageTime(LocalDateTime.parse(lastMessage?.createdAt ?: "2022-05-12T12:30:33")), // 수정 필요 LocalDateTimeFormatter.parse 사용할 것
            isChatAvailable,
        )
    }

    private fun formatLastMessageTime(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")
        return dateTime.format(formatter)
    }
}
