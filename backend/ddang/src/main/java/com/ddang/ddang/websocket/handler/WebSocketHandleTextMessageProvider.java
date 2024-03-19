package com.ddang.ddang.websocket.handler;

import com.ddang.ddang.websocket.handler.dto.SendMessageDto;
import com.ddang.ddang.websocket.handler.dto.TextMessageType;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;

public interface WebSocketHandleTextMessageProvider {

    TextMessageType supportTextMessageType();

    List<SendMessageDto> handleCreateSendMessage(
            final WebSocketSession session,
            final Map<String, String> data
    ) throws Exception;

    void remove(final WebSocketSession session);
}