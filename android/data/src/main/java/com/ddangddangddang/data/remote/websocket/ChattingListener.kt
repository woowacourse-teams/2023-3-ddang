package com.ddangddangddang.data.remote.websocket

import android.util.Log
import com.ddangddangddang.data.model.response.ChatMessageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChattingListener : WebSocketListener() {
    private val _message: MutableStateFlow<MessageCallType> =
        MutableStateFlow(MessageCallType.None)
    val message: StateFlow<MessageCallType>
        get() = _message.asStateFlow()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d(LOG_TAG, "Chatting WebSocket Open")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d(LOG_TAG, "Chatting WebSocket Message : $text")
        val message = Json.decodeFromString<ChatMessageResponse>(text)
        _message.update { MessageCallType.Success(message) }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d(LOG_TAG, "Chatting WebSocket Failed : ${t.message}")
        _message.update { MessageCallType.Failure }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d(LOG_TAG, "Chatting WebSocket Closed")
        _message.update { MessageCallType.None }
    }

    sealed interface MessageCallType {
        object None : MessageCallType
        data class Success(val message: ChatMessageResponse) : MessageCallType
        object Failure : MessageCallType
    }

    companion object {
        private const val LOG_TAG = "ChattingWebSocket"
    }
}
