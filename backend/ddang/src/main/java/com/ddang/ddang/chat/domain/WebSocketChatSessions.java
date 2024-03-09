package com.ddang.ddang.chat.domain;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class WebSocketChatSessions {

    private static final String ATTRIBUTE_KEY = "chatRoomId";
    private final Map<Long, WebSocketSessions> chatRoomSessions;

    public WebSocketChatSessions() {
        this.chatRoomSessions = new ConcurrentHashMap<>();
    }

    public void add(final WebSocketSession session, final Long chatRoomId) {
        chatRoomSessions.putIfAbsent(chatRoomId, new WebSocketSessions());
        final WebSocketSessions webSocketSessions = chatRoomSessions.get(chatRoomId);
        if (!webSocketSessions.containsValue(session)) {
            webSocketSessions.add(session);
            session.getAttributes().put(ATTRIBUTE_KEY, chatRoomId);
        }
    }

    public void remove(final WebSocketSession session) {
        final long chatRoomId = Long.parseLong(String.valueOf(session.getAttributes().get(ATTRIBUTE_KEY)));
        final WebSocketSessions webSocketSessions = chatRoomSessions.get(chatRoomId);
        webSocketSessions.remove(session);
    }
}
