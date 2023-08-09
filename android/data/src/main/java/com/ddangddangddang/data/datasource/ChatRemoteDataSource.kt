package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.request.CreateChatRoomRequest
import com.ddangddangddang.data.model.response.ChatMessageIdResponse
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.Service

class ChatRemoteDataSource(private val service: Service) {
    suspend fun createChatRoom(createChatRoomRequest: CreateChatRoomRequest): ApiResponse<Unit> =
        service.createChatRoom(createChatRoomRequest)

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
