package com.ddang.ddang.chat.domain;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WebSocketSessions {

    private final Set<WebSocketSession> sessions;

    public WebSocketSessions() {
        this.sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void add(final WebSocketSession session) {
        sessions.add(session);
    }

    public void remove(final WebSocketSession session) {
        sessions.remove(session);
    }
}
