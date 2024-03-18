package com.ddangddangddang.data.model.request

sealed class WebSocketRequest(val type: String, open val data: WebSocketDataRequest) {

    data class ChatMessageRequest(
        override val data: WebSocketDataRequest.ChatMessageDataRequest,
    ) : WebSocketRequest("CHATTINGS", data)

    sealed class WebSocketDataRequest {
        data class ChatMessageDataRequest(
            val chatRoomId: Long,
            val receiverId: Long,
            val contents: String,
        ) : WebSocketDataRequest()
    }
}
