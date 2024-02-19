package com.ddang.ddang.chat.configuration;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class WebSocketChatInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            final ServerHttpRequest request,
            final ServerHttpResponse response,
            final WebSocketHandler wsHandler,
            final Map<String, Object> attributes
    ) throws Exception {
        final UriComponents uriComponents = UriComponentsBuilder.fromUri(request.getURI()).build();
        final String chatRoomId = uriComponents.getQueryParams()
                .getFirst("chatRoomId");
        attributes.put("chatRoomId", chatRoomId);

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
