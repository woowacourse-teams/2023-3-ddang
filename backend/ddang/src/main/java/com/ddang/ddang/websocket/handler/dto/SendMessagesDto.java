package com.ddang.ddang.websocket.handler.dto;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public record SendMessagesDto(WebSocketSession session, TextMessage textMessage) {
}
