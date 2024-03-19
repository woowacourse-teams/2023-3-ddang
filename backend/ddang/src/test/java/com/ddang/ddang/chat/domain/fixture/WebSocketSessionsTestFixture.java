package com.ddang.ddang.chat.domain.fixture;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class WebSocketSessionsTestFixture {

    protected Long 사용자_아이디 = 1L;
    protected Map<String, Object> 세션_attribute_정보 = new HashMap<>(Map.of("userId", 사용자_아이디, "baseUrl", "/images"));
    protected Long 채팅방_아이디 = 1L;
}
