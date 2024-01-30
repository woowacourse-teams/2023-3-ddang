package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.ChatRemoteDataSource
import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.request.GetChatRoomIdRequest
import com.ddangddangddang.data.model.response.ChatMessageIdResponse
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.model.response.ChatRoomIdResponse
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import com.ddangddangddang.data.model.response.ChatRoomResponse
import com.ddangddangddang.data.remote.ApiResponse
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource,
) : ChatRepository {
    override suspend fun getChatRoomId(getChatRoomIdRequest: GetChatRoomIdRequest): ApiResponse<ChatRoomIdResponse> =
        chatRemoteDataSource.getChatRoomId(getChatRoomIdRequest)

    override suspend fun getChatRoomPreviews(): ApiResponse<List<ChatRoomPreviewResponse>> =
        chatRemoteDataSource.getChatRoomPreviews()

    override suspend fun getChatRoom(chatRoomId: Long): ApiResponse<ChatRoomResponse> =
        chatRemoteDataSource.getChatRoom(chatRoomId)

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
