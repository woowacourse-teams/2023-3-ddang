package com.ddang.ddang.websocket.handler;

import com.ddang.ddang.chat.handler.ChatWebSocketHandleTextMessageProvider;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.websocket.handler.dto.SendMessageDto;
import com.ddang.ddang.websocket.handler.dto.TextMessageType;
import com.ddang.ddang.websocket.handler.fixture.WebSocketHandlerTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class WebSocketHandlerTest extends WebSocketHandlerTestFixture {

    @MockBean
    ChatWebSocketHandleTextMessageProvider provider;

    @MockBean
    WebSocketHandleTextMessageProviderComposite providerComposite;

    @Autowired
    WebSocketHandler webSocketHandler;

    @Mock
    WebSocketSession session;

    @Test
    void 세션의_타입_속성이_채팅이라면_채팅_provider를_통해_메시지를_생성한다() throws Exception {
        // given
        given(providerComposite.findProvider(TextMessageType.CHATTINGS)).willReturn(provider);
        given(session.getAttributes()).willReturn(세션_attribute_정보);
        given(provider.handleCreateSendMessage(any(WebSocketSession.class), anyMap()))
                .willReturn(List.of(new SendMessageDto(session, 전송할_메시지)));

        // when
        final TextMessage message = new TextMessage(new ObjectMapper().writeValueAsString(세션_attribute_정보));
        webSocketHandler.handleTextMessage(session, message);

        // then
        verify(provider, times(1)).handleCreateSendMessage(any(WebSocketSession.class), anyMap());
        verify(session, times(1)).sendMessage(any());
    }

    @Test
    void 웹소켓_통신이_종료되면_해당_세션의_타입에_따라_해당하는_provider를_통해_세션을_제거한다() {
        // given
        given(providerComposite.findProvider(TextMessageType.CHATTINGS)).willReturn(provider);
        given(session.getAttributes()).willReturn(세션_attribute_정보);

        // when
        webSocketHandler.afterConnectionClosed(session, null);

        // then
        verify(provider, times(1)).remove(any(WebSocketSession.class));
    }
}
