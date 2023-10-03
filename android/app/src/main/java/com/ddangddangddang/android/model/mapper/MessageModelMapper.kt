package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.MessageModel
import com.ddangddangddang.data.model.response.ChatMessageResponse

object MessageModelMapper : Mapper<MessageModel, ChatMessageResponse> {
    override fun ChatMessageResponse.toPresentation(): MessageModel {
        return MessageModel(
            id,
            createdAt,
            isMyMessage,
            contents,
        )
    }
}
