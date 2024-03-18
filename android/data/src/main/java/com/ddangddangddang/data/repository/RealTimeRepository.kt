package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.request.WebSocketRequest
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.flow.Flow

interface RealTimeRepository {
    fun sendMessage(
        data: WebSocketRequest.WebSocketDataRequest.ChatMessageDataRequest,
    ): Boolean

    fun observeChatMessage(): Flow<ChatMessageResponse>
    fun observeWebSocketEvent(): Flow<WebSocket.Event>
}
