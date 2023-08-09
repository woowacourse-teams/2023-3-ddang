package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.ChatRemoteDataSource
import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.request.CreateChatRoomRequest
import com.ddangddangddang.data.model.response.ChatMessageIdResponse
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import com.ddangddangddang.data.remote.ApiResponse

class ChatRepositoryImpl(
    private val chatRemoteDataSource: ChatRemoteDataSource,
) : ChatRepository {
    override suspend fun createChatRoom(createChatRoomRequest: CreateChatRoomRequest): ApiResponse<Unit> =
        chatRemoteDataSource.createChatRoom(createChatRoomRequest)

    override suspend fun getChatRoomPreviews(): ApiResponse<List<ChatRoomPreviewResponse>> =
        chatRemoteDataSource.getChatRoomPreviews()

    override suspend fun getChatRoomPreview(chatRoomId: Long): ApiResponse<ChatRoomPreviewResponse> =
        chatRemoteDataSource.getChatRoomPreview(chatRoomId)

    override suspend fun getMessages(
        chatRoomId: Long,
        lastMessageId: Long?,
    ): ApiResponse<List<ChatMessageResponse>> =
        chatRemoteDataSource.getMessages(chatRoomId, lastMessageId)

    override suspend fun sendMessage(
        chatRoomId: Long,
        chatMessageRequest: ChatMessageRequest,
    ): ApiResponse<ChatMessageIdResponse> =
        chatRemoteDataSource.sendMessage(chatRoomId, chatMessageRequest)
}
