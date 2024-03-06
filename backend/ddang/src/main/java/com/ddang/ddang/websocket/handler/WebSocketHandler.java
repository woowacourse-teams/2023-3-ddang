package com.ddang.ddang.websocket.handler;

import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.domain.WebSocketChatSessions;
import com.ddang.ddang.chat.domain.WebSocketSessions;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.chat.presentation.dto.response.ReadMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketChatSessions sessions;
    private final ObjectMapper objectMapper;
    private final MessageService messageService;

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        // TODO: 2024/03/04 리팩토링 역할 분리 필요
        final String payload = message.getPayload();
        final CreateMessageRequest request = objectMapper.readValue(payload, CreateMessageRequest.class);
        final Map<String, Object> attributes = session.getAttributes();
        final Long userId = Long.parseLong(String.valueOf(attributes.get("userId")));
        final Long groupId = Long.parseLong(String.valueOf(attributes.get("groupId")));
        final CreateMessageDto createMessageDto = CreateMessageDto.of(userId, groupId, request);

        final String baseUrl = String.valueOf(attributes.get("baseUrl"));
        final Long messageId = messageService.create(createMessageDto, baseUrl);
        final Map<Long, WebSocketSessions> chatRoomSessions = sessions.getChatRoomSessions();
        final WebSocketSessions webSocketSessions = chatRoomSessions.get(groupId);
        final Set<WebSocketSession> groupSessions = webSocketSessions.getSessions();
        for (WebSocketSession currentSession : groupSessions) {
            ReadMessageResponse response;
            if (currentSession.getAttributes().get("userId") == userId) {
                response = new ReadMessageResponse(messageId, LocalDateTime.now(), true, request.contents());
            } else {
                response = new ReadMessageResponse(messageId, LocalDateTime.now(), false, request.contents());
            }
            final TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(response));
            currentSession.sendMessage(textMessage);
        }
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
