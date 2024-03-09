package com.ddang.ddang.websocket.handler;

import com.ddang.ddang.websocket.handler.dto.SendMessagesDto;
import com.ddang.ddang.websocket.handler.dto.TextMessageDto;
import com.ddang.ddang.websocket.handler.dto.TextMessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private static final String TYPE_KEY = "type";

    private final WebSocketHandleTextMessageProviderComposite providerComposite;
    private final ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        final String payload = message.getPayload();
        final TextMessageDto textMessageDto = objectMapper.readValue(payload, TextMessageDto.class);
        session.getAttributes().put(TYPE_KEY, textMessageDto.type());

        final WebSocketHandleTextMessageProvider provider = providerComposite.findProvider(textMessageDto.type());
        final List<SendMessagesDto> sendMessagesDtos = provider.handle(session, textMessageDto.data());
        for (SendMessagesDto sendMessagesDto : sendMessagesDtos) {
            sendMessagesDto.session()
                           .sendMessage(sendMessagesDto.textMessage());
        }
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        final String type = String.valueOf(session.getAttributes().get(TYPE_KEY));
        final WebSocketHandleTextMessageProvider provider = providerComposite.findProvider(TextMessageType.valueOf(type));
        provider.remove(session);
    }
}
