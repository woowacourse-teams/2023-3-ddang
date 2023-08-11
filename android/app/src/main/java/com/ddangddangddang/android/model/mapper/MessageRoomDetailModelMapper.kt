package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.MessageRoomDetailModel
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse

object MessageRoomDetailModelMapper : Mapper<MessageRoomDetailModel, ChatRoomPreviewResponse> {
    override fun ChatRoomPreviewResponse.toPresentation(): MessageRoomDetailModel {
        return MessageRoomDetailModel(
            id,
            auction.id,
            auction.title,
            auction.image,
            auction.price,
            chatPartner.id,
            chatPartner.name,
            chatPartner.profileImage ?: "",
            isChatAvailable,
        )
    }
}
