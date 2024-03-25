package com.ddang.ddang.websocket.handler;

import com.ddang.ddang.websocket.handler.dto.TextMessageType;
import com.ddang.ddang.websocket.handler.exception.UnsupportedTextMessageTypeException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WebSocketHandleTextMessageProviderComposite {

    private final Map<TextMessageType, WebSocketHandleTextMessageProvider> mappings;

    public WebSocketHandleTextMessageProviderComposite(final Set<WebSocketHandleTextMessageProvider> providers) {
        this.mappings = providers.stream()
                                 .collect(Collectors.toMap(
                                         WebSocketHandleTextMessageProvider::supportTextMessageType,
                                         provider -> provider
                                 ));
    }

    public WebSocketHandleTextMessageProvider findProvider(final TextMessageType textMessageType) {
        final WebSocketHandleTextMessageProvider provider = mappings.get(textMessageType);

        if (provider == null) {
            throw new UnsupportedTextMessageTypeException("지원하는 웹 소켓 통신 타입이 아닙니다.");
        }

        return provider;
    }
}
