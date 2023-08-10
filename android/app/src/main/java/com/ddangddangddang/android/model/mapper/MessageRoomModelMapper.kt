package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.MessageRoomModel
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MessageRoomModelMapper : Mapper<MessageRoomModel, ChatRoomPreviewResponse> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    override fun ChatRoomPreviewResponse.toPresentation(): MessageRoomModel {
        return MessageRoomModel(
            id,
            auction.title,
            chatPartner.profileImage,
            chatPartner.name,
            "마지막 메시지 내용", // 수정 필요
            LocalDateTime.now(), // 변환 필요
            isChatAvailable,
        )
    }
}
