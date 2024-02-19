package com.ddang.ddang.chat.configuration;

import com.ddang.ddang.chat.handler.WebSocketChatHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final WebSocketChatHandler handler;
    private final WebSocketChatInterceptor interceptor;

    public WebSocketConfiguration(final WebSocketChatHandler handler, final WebSocketChatInterceptor interceptor) {
        this.handler = handler;
        this.interceptor = interceptor;
    }

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/sub/chattings")
                .addInterceptors(interceptor);
    }
}
