package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.ChatRoomModel
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ChatRoomModelMapper : Mapper<ChatRoomModel, ChatRoomPreviewResponse> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    override fun ChatRoomPreviewResponse.toPresentation(): ChatRoomModel {
        return ChatRoomModel(
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
