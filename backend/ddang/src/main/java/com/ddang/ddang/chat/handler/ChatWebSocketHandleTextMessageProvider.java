package com.ddang.ddang.chat.handler;

import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.domain.WebSocketChatSessions;
import com.ddang.ddang.chat.domain.WebSocketSessions;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.chat.presentation.dto.response.ReadMessageResponse;
import com.ddang.ddang.websocket.handler.WebSocketHandleTextMessageProvider;
import com.ddang.ddang.websocket.handler.dto.SendMessagesDto;
import com.ddang.ddang.websocket.handler.dto.TextMessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandleTextMessageProvider implements WebSocketHandleTextMessageProvider {

    private final WebSocketChatSessions sessions;
    private final ObjectMapper objectMapper;
    private final MessageService messageService;

    @Override
    public TextMessageType supportTextMessageType() {
        return TextMessageType.CHATTINGS;
    }

    @Override
    public List<SendMessagesDto> handle(final WebSocketSession session, final Map<String, String> data) throws Exception {
        final CreateMessageRequest request = new CreateMessageRequest(
                Long.parseLong(data.get("recevierId")),
                data.get("contents")
        );

        final long chatRoomId = Long.parseLong(data.get("chatRoomId"));
        sessions.add(session, chatRoomId);

        final Map<String, Object> attributes = session.getAttributes();
        final Long userId = Long.parseLong(String.valueOf(attributes.get("userId")));
        final CreateMessageDto createMessageDto = CreateMessageDto.of(userId, chatRoomId, request);

        final String baseUrl = String.valueOf(attributes.get("baseUrl"));
        final Long messageId = messageService.create(createMessageDto, baseUrl);
        final Map<Long, WebSocketSessions> chatRoomSessions = sessions.getChatRoomSessions();
        final WebSocketSessions webSocketSessions = chatRoomSessions.get(chatRoomId);
        final Set<WebSocketSession> groupSessions = webSocketSessions.getSessions();
        final List<SendMessagesDto> sendMessagesDtos = new ArrayList<>();
        for (WebSocketSession currentSession : groupSessions) {
            ReadMessageResponse response;
            if (currentSession.getAttributes().get("userId") == userId) {
                response = new ReadMessageResponse(messageId, LocalDateTime.now(), true, request.contents());
            } else {
                response = new ReadMessageResponse(messageId, LocalDateTime.now(), false, request.contents());
            }
            final TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(response));
            sendMessagesDtos.add(new SendMessagesDto(session, textMessage));
        }

        return sendMessagesDtos;
    }

    @Override
    public void remove(final WebSocketSession session) {
        sessions.remove(session);
    }
}
