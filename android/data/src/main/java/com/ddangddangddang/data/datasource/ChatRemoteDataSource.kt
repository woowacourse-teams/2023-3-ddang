package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.request.GetChatRoomIdRequest
import com.ddangddangddang.data.model.response.ChatMessageIdResponse
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.model.response.ChatRoomIdResponse
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import com.ddangddangddang.data.model.response.ChatRoomResponse
import com.ddangddangddang.data.remote.callAdapter.ApiResponse
import com.ddangddangddang.data.remote.retrofit.AuctionService
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(private val service: AuctionService) {
    suspend fun getChatRoomId(getChatRoomIdRequest: GetChatRoomIdRequest): ApiResponse<ChatRoomIdResponse> =
        service.getChatRoomId(getChatRoomIdRequest)

    suspend fun getChatRoomPreviews(): ApiResponse<List<ChatRoomPreviewResponse>> =
        service.getChatRoomPreviews()

    suspend fun getChatRoom(chatRoomId: Long): ApiResponse<ChatRoomResponse> =
        service.getChatRoom(chatRoomId)

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
