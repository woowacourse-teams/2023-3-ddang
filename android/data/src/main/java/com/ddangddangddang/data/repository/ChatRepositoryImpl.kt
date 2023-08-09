package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.ChatRemoteDataSource
import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.request.GetChatRoomIdRequest
import com.ddangddangddang.data.model.response.ChatMessageIdResponse
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.model.response.ChatRoomIdResponse
import com.ddangddangddang.data.model.response.ChatRoomPreviewResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.Service

class ChatRepositoryImpl private constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource,
) : ChatRepository {
    override suspend fun getChatRoomId(getChatRoomIdRequest: GetChatRoomIdRequest): ApiResponse<ChatRoomIdResponse> =
        chatRemoteDataSource.getChatRoomId(getChatRoomIdRequest)

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

    companion object {
        @Volatile
        private var instance: ChatRepositoryImpl? = null

        fun getInstance(service: Service): ChatRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: createInstance(service)
            }
        }

        private fun createInstance(service: Service): ChatRepositoryImpl {
            val remoteDataSource = ChatRemoteDataSource(service)
            return ChatRepositoryImpl(remoteDataSource)
                .also { instance = it }
        }
    }
}
