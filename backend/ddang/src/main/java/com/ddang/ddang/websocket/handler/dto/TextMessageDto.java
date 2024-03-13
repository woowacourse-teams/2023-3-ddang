package com.ddang.ddang.websocket.handler.dto;

import java.util.Map;

public record TextMessageDto(TextMessageType type, Map<String, String> data) {
}
