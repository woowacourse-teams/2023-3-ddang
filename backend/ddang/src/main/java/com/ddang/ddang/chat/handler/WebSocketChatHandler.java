package com.ddang.ddang.chat.handler;

import com.ddang.ddang.chat.domain.WebSocketChatSessions;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final WebSocketChatSessions sessions;

    public WebSocketChatHandler() {
        this.sessions = new WebSocketChatSessions();
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        sessions.add(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }
}
