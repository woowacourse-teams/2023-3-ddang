package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.MessageModel
import com.ddangddangddang.data.model.response.ChatMessageResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MessageModelMapper : Mapper<MessageModel, ChatMessageResponse> {
    private val formatter = DateTimeFormatter.ofPattern("h:mm a")
    override fun ChatMessageResponse.toPresentation(): MessageModel {
        return MessageModel(
            id,
            LocalDateTime.parse(createdAt).format(formatter),
            isMyMessage,
            contents,
        )
    }
}
