package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.request.GetChatRoomIdRequest
import com.ddangddangddang.data.model.response.ChatMessageIdResponse
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.model.response.ChatRoomIdResponse
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import com.ddangddangddang.data.model.response.ChatRoomResponse
import com.ddangddangddang.data.remote.ApiResponse

interface ChatRepository {
    suspend fun getChatRoomId(getChatRoomIdRequest: GetChatRoomIdRequest): ApiResponse<ChatRoomIdResponse>
    suspend fun getChatRoomPreviews(): ApiResponse<List<ChatRoomPreviewResponse>>
    suspend fun getChatRoom(chatRoomId: Long): ApiResponse<ChatRoomResponse>
    suspend fun getMessages(
        chatRoomId: Long,
        lastMessageId: Long?,
    ): ApiResponse<List<ChatMessageResponse>>

    suspend fun sendMessage(
        chatRoomId: Long,
        chatMessageRequest: ChatMessageRequest,
    ): ApiResponse<ChatMessageIdResponse>
}
