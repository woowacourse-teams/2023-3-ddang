package com.ddang.ddang.chat.handler;

import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.domain.WebSocketChatSessions;
import com.ddang.ddang.chat.domain.WebSocketSessions;
import com.ddang.ddang.chat.handler.dto.ChatMessageDataDto;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.chat.presentation.dto.response.ReadMessageResponse;
import com.ddang.ddang.websocket.handler.WebSocketHandleTextMessageProvider;
import com.ddang.ddang.websocket.handler.dto.SendMessagesDto;
import com.ddang.ddang.websocket.handler.dto.SessionAttributeDto;
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
        final SessionAttributeDto sessionAttribute = getSessionAttributes(session);
        final ChatMessageDataDto messageData = objectMapper.convertValue(data, ChatMessageDataDto.class);
        sessions.add(session, messageData.chatRoomId());

        final Long senderId = sessionAttribute.userId();
        final CreateMessageDto createMessageDto = createMessageDto(messageData, senderId);

        final Long messageId = messageService.create(createMessageDto, sessionAttribute.baseUrl());
        final Map<Long, WebSocketSessions> chatRoomSessions = sessions.getChatRoomSessions();
        final WebSocketSessions webSocketSessions = chatRoomSessions.get(messageData.chatRoomId());
        final Set<WebSocketSession> groupSessions = webSocketSessions.getSessions();
        final List<SendMessagesDto> sendMessagesDtos = new ArrayList<>();
        for (WebSocketSession currentSession : groupSessions) {
            ReadMessageResponse response;
            if (currentSession.getAttributes().get("userId") == senderId) {
                response = new ReadMessageResponse(messageId, LocalDateTime.now(), true, messageData.contents());
            } else {
                response = new ReadMessageResponse(messageId, LocalDateTime.now(), false, messageData.contents());
            }
            final TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(response));
            sendMessagesDtos.add(new SendMessagesDto(session, textMessage));
        }

        return sendMessagesDtos;
    }

    private SessionAttributeDto getSessionAttributes(final WebSocketSession session) {
        final Map<String, Object> attributes = session.getAttributes();

        return objectMapper.convertValue(attributes, SessionAttributeDto.class);
    }

    private CreateMessageDto createMessageDto(final ChatMessageDataDto messageData, final Long userId) {
        final CreateMessageRequest request = new CreateMessageRequest(
                messageData.receiverId(),
                messageData.contents()
        );

        return CreateMessageDto.of(userId, messageData.chatRoomId(), request);
    }

    @Override
    public void remove(final WebSocketSession session) {
        sessions.remove(session);
    }
}
