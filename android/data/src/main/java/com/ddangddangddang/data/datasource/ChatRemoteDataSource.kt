package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.request.GetChatRoomIdRequest
import com.ddangddangddang.data.model.response.ChatMessageIdResponse
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.model.response.ChatRoomIdResponse
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuctionService

class ChatRemoteDataSource(private val service: AuctionService) {
    suspend fun getChatRoomId(getChatRoomIdRequest: GetChatRoomIdRequest): ApiResponse<ChatRoomIdResponse> =
        service.getChatRoomId(getChatRoomIdRequest)

    suspend fun getChatRoomPreviews(): ApiResponse<List<ChatRoomPreviewResponse>> =
        service.getChatRoomPreviews()

    suspend fun getChatRoomPreview(chatRoomId: Long): ApiResponse<ChatRoomPreviewResponse> =
        service.getChatRoomPreview(chatRoomId)

    suspend fun getMessages(
        chatRoomId: Long,
        lastMessageId: Long?,
    ): ApiResponse<List<ChatMessageResponse>> =
        service.getMessages(chatRoomId, lastMessageId)

    suspend fun sendMessage(
        chatRoomId: Long,
        chatMessageRequest: ChatMessageRequest,
    ): ApiResponse<ChatMessageIdResponse> =
        service.sendMessage(chatRoomId, chatMessageRequest)
}
