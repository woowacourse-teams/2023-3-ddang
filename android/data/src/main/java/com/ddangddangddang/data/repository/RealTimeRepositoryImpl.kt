package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.request.WebSocketRequest
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.ddangddangddang.data.remote.scarlet.WebSocketService
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RealTimeRepositoryImpl @Inject constructor(
    private val service: WebSocketService,
) : RealTimeRepository {
    override fun sendMessage(data: WebSocketRequest.WebSocketDataRequest.ChatMessageDataRequest): Boolean {
        val request = WebSocketRequest.ChatMessageRequest(data)
        return service.sendMessage(request)
    }

    override fun observeChatMessage(): Flow<ChatMessageResponse> = service.observeChatMessage()

    override fun observeWebSocketEvent(): Flow<WebSocket.Event> = service.observeWebSocketEvent()
}
