package com.ddang.ddang.chat.domain;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class WebSocketChatSessions {

    private final Map<Long, WebSocketSessions> chatRoomSessions;

    public WebSocketChatSessions() {
        this.chatRoomSessions = new ConcurrentHashMap<>();
    }

    public void add(final WebSocketSession session, final Long chatRoomId) {
        chatRoomSessions.putIfAbsent(chatRoomId, new WebSocketSessions());
        final WebSocketSessions webSocketSessions = chatRoomSessions.get(chatRoomId);
        if (!webSocketSessions.containsValue(session)) {
            webSocketSessions.add(session);
        }
    }

//    private static long parseChatRoomId(final WebSocketSession session) {
//        return Long.parseLong((String) session.getAttributes().get("groupId"));
//    }
//
//    public void remove(final WebSocketSession session) {
//        final long chatRoomId = parseChatRoomId(session);
//        final WebSocketSessions webSocketSessions = chatRoomSessions.get(chatRoomId);
//        webSocketSessions.remove(session);
//    }
}
