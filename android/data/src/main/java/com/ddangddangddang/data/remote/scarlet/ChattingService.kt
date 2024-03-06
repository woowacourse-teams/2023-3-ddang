package com.ddangddangddang.data.remote.scarlet

import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.model.response.ChatMessageResponse
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow

interface ChattingService {
    @Send
    fun sendChatMessage(chatMessage: ChatMessageRequest): Boolean

    @Receive
    fun observeChatMessage(): Flow<ChatMessageResponse>
}
