package com.ddang.ddang.chat.domain;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WebSocketSessions {

    protected static final String ATTRIBUTE_KEY = "chatRoomId";

    private final Set<WebSocketSession> sessions;

    public WebSocketSessions() {
        this.sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void putIfAbsent(final WebSocketSession session, final Long chatRoomId) {
        if (!sessions.contains(session)) {
            sessions.add(session);
            session.getAttributes().put(ATTRIBUTE_KEY, chatRoomId);
        }
    }

    public void remove(final WebSocketSession session) {
        sessions.remove(session);
    }

    public boolean contains(final Long userId) {
        return sessions.stream()
                       .anyMatch(session -> session.getAttributes().get("userId") == userId);
    }
}
