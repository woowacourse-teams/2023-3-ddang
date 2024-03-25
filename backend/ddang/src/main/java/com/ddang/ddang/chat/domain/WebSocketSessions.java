package com.ddang.ddang.chat.domain;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WebSocketSessions {

    protected static final String CHAT_ROOM_ID_KEY = "chatRoomId";
    private static final String USER_ID_KEY = "userId";

    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void putIfAbsent(final WebSocketSession session, final Long chatRoomId) {
        if (!sessions.contains(session)) {
            session.getAttributes().put(CHAT_ROOM_ID_KEY, chatRoomId);
            sessions.add(session);
        }
    }

    public boolean contains(final Long userId) {
        return sessions.stream()
                       .anyMatch(session -> session.getAttributes().get(USER_ID_KEY) == userId);
    }

    public void remove(final WebSocketSession session) {
        sessions.remove(session);
    }
}
