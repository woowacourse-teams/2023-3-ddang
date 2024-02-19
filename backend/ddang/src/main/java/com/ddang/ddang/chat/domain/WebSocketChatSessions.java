package com.ddang.ddang.chat.domain;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketChatSessions {

    private final Map<Long, WebSocketSessions> chatRoomSessions;

    public WebSocketChatSessions() {
        this.chatRoomSessions = new ConcurrentHashMap<>();
    }

    public void add(final WebSocketSession session) {
        final long chatRoomId = Long.parseLong((String) session.getAttributes().get("chatRoomId"));
        chatRoomSessions.putIfAbsent(chatRoomId, new WebSocketSessions());
        final WebSocketSessions webSocketSessions = chatRoomSessions.get(chatRoomId);
        webSocketSessions.add(session);
    }
}
