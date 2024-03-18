package com.ddangddangddang.data.remote.scarlet

import com.ddangddangddang.data.model.request.WebSocketRequest
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow

interface WebSocketService {
    @Send
    fun sendMessage(message: WebSocketRequest): Boolean

    @Receive
    fun observeChatMessage(): Flow<ChatMessageResponse>

    @Receive
    fun observeWebSocketEvent(): Flow<WebSocket.Event>
}
