package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.request.CreateChatRoomRequest
import com.ddangddangddang.data.model.response.ChatMessageIdResponse
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import com.ddangddangddang.data.remote.ApiResponse

interface ChatRepository {
    suspend fun createChatRoom(createChatRoomRequest: CreateChatRoomRequest): ApiResponse<Unit>
    suspend fun getChatRoomPreviews(): ApiResponse<List<ChatRoomPreviewResponse>>
    suspend fun getChatRoomPreview(chatRoomId: Long): ApiResponse<ChatRoomPreviewResponse>
    suspend fun getMessages(
        chatRoomId: Long,
        lastMessageId: Long?,
    ): ApiResponse<List<ChatMessageResponse>>

    suspend fun sendMessage(
        chatRoomId: Long,
        chatMessageRequest: ChatMessageRequest,
    ): ApiResponse<ChatMessageIdResponse>
}
