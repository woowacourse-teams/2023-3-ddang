package com.ddang.ddang.websocket.handler.fixture;

import com.ddang.ddang.websocket.handler.dto.TextMessageType;
import org.springframework.web.socket.TextMessage;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class WebSocketHandlerTestFixture {

    protected Long 사용자_아이디 = 1L;
    protected Map<String, Object> 세션_attribute_정보 = new HashMap<>(
            Map.of(
                    "type", TextMessageType.CHATTINGS.name(),
                    "data", Map.of("userId", 사용자_아이디, "baseUrl", "/images")
            )
    );
    protected TextMessage 전송할_메시지 = new TextMessage("메시지");
}
