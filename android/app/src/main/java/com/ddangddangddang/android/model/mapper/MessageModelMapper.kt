package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.MessageModel
import com.ddangddangddang.data.model.response.ChatMessageResponse
import java.time.LocalDateTime

object MessageModelMapper : Mapper<MessageModel, ChatMessageResponse> {
    override fun ChatMessageResponse.toPresentation(): MessageModel {
        return MessageModel(
            id,
            LocalDateTime.parse(createdAt),
            isMyMessage,
            contents,
        )
    }
}
