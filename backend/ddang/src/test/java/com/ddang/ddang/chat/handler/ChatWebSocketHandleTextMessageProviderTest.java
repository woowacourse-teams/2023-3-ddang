package com.ddang.ddang.chat.handler;

import com.ddang.ddang.chat.application.event.MessageNotificationEvent;
import com.ddang.ddang.chat.application.event.UpdateReadMessageLogEvent;
import com.ddang.ddang.chat.domain.WebSocketChatSessions;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import com.ddang.ddang.chat.handler.fixture.ChatWebSocketHandleTextMessageProviderTestFixture;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.notification.application.NotificationService;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationStatus;
import com.ddang.ddang.websocket.handler.dto.SendMessageDto;
import com.ddang.ddang.websocket.handler.dto.TextMessageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@IsolateDatabase
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatWebSocketHandleTextMessageProviderTest extends ChatWebSocketHandleTextMessageProviderTestFixture {

    @Autowired
    ChatWebSocketHandleTextMessageProvider provider;

    @Autowired
    ReadMessageLogRepository readMessageLogRepository;

    @SpyBean
    WebSocketChatSessions sessions;

    @Mock
    WebSocketSession writerSession;

    @Mock
    WebSocketSession receiverSession;

    @MockBean
    NotificationService notificationService;

    @Autowired
    ApplicationEvents events;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 지원하는_웹소켓_핸들링_타입을_반환한다() {
        // when
        final TextMessageType actual = provider.supportTextMessageType();

        // then
        assertThat(actual).isEqualTo(TextMessageType.CHATTINGS);
    }

    @Test
    void 메시지_생성시_수신자가_웹소켓_통신_중이라면_발신자_수신자_모두에게_메시지를_보낸다() throws JsonProcessingException {
        // given
        given(writerSession.getAttributes()).willReturn(발신자_세션_attribute_정보);
        given(receiverSession.getAttributes()).willReturn(수신자_세션_attribute_정보);
        willDoNothing().given(sessions).add(writerSession, 채팅방.getId());
        willReturn(true).given(sessions).containsByUserId(채팅방.getId(), 수신자.getId());
        willReturn(Set.of(writerSession, receiverSession)).given(sessions).getSessionsByChatRoomId(채팅방.getId());

        // when
        final List<SendMessageDto> actual = provider.handleCreateSendMessage(writerSession, 메시지_전송_데이터);

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 메시지_생성시_수신자가_웹소켓_통신_중이라면_메시지_로그_업데이트_메서드를_호출한다() throws JsonProcessingException {
        // given
        메시지_로그를_생성한다();

        given(writerSession.getAttributes()).willReturn(발신자_세션_attribute_정보);
        given(receiverSession.getAttributes()).willReturn(수신자_세션_attribute_정보);
        willDoNothing().given(sessions).add(writerSession, 채팅방.getId());
        willReturn(true).given(sessions).containsByUserId(채팅방.getId(), 수신자.getId());
        willReturn(Set.of(writerSession, receiverSession)).given(sessions).getSessionsByChatRoomId(채팅방.getId());

        // when
        provider.handleCreateSendMessage(writerSession, 메시지_전송_데이터);
        final long actual = events.stream(UpdateReadMessageLogEvent.class).count();

        // then
        assertThat(actual).isEqualTo(2L);
    }

    @Test
    void 메시지_생성시_수신자가_웹소켓_통신중이지_않다면_발신자의_메시지_로그_업데이트_메서드만_호출한다() throws JsonProcessingException {
        // given
        메시지_로그를_생성한다();

        given(writerSession.getAttributes()).willReturn(발신자_세션_attribute_정보);
        willDoNothing().given(sessions).add(writerSession, 채팅방.getId());
        willReturn(false).given(sessions).containsByUserId(채팅방.getId(), 수신자.getId());
        willReturn(Set.of(writerSession)).given(sessions).getSessionsByChatRoomId(채팅방.getId());

        // when
        provider.handleCreateSendMessage(writerSession, 메시지_전송_데이터);
        final long actual = events.stream(UpdateReadMessageLogEvent.class).count();

        // then
        assertThat(actual).isEqualTo(1L);
    }

    @Test
    void 메시지_생성시_수신자가_웹소켓_통신_중이지_않다면_발신자에게만_메시지를_전달한다() throws JsonProcessingException {
        // given
        given(writerSession.getAttributes()).willReturn(발신자_세션_attribute_정보);
        willDoNothing().given(sessions).add(writerSession, 채팅방.getId());
        willReturn(false).given(sessions).containsByUserId(채팅방.getId(), 수신자.getId());
        willReturn(Set.of(writerSession)).given(sessions).getSessionsByChatRoomId(채팅방.getId());

        // when
        final List<SendMessageDto> actual = provider.handleCreateSendMessage(writerSession, 메시지_전송_데이터);

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void 메시지_생성시_수신자가_웹소켓_통신_중이지_않다면_수신자에게_알림을_보낸다() throws Exception {
        // given
        given(writerSession.getAttributes()).willReturn(발신자_세션_attribute_정보);
        willDoNothing().given(sessions).add(writerSession, 채팅방.getId());
        willReturn(false).given(sessions).containsByUserId(채팅방.getId(), 수신자.getId());
        willReturn(Set.of(writerSession)).given(sessions).getSessionsByChatRoomId(채팅방.getId());
        given(notificationService.send(any(CreateNotificationDto.class))).willReturn(NotificationStatus.SUCCESS);

        // when
        provider.handleCreateSendMessage(writerSession, 메시지_전송_데이터);
        final long actual = events.stream(MessageNotificationEvent.class).count();

        // then
        assertThat(actual).isEqualTo(1);
    }

    @Test
    void 메시지_생성시_수신자에게_알림을_보내기에_실패하더라도_정상적으로_메시지가_전달된다() throws Exception {
        // given
        given(writerSession.getAttributes()).willReturn(발신자_세션_attribute_정보);
        willDoNothing().given(sessions).add(writerSession, 채팅방.getId());
        willReturn(false).given(sessions).containsByUserId(채팅방.getId(), 수신자.getId());
        willReturn(Set.of(writerSession)).given(sessions).getSessionsByChatRoomId(채팅방.getId());
        given(notificationService.send(any(CreateNotificationDto.class))).willReturn(NotificationStatus.FAIL);

        // when
        final List<SendMessageDto> actual = provider.handleCreateSendMessage(writerSession, 메시지_전송_데이터);

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void 세션을_삭제한다() {
        // given
        given(writerSession.getAttributes()).willReturn(발신자_세션_attribute_정보);
        sessions.add(writerSession, 채팅방.getId());

        // when
        provider.remove(writerSession);

        // then
        final boolean actual = sessions.containsByUserId(채팅방.getId(), 발신자.getId());
        assertThat(actual).isFalse();
    }
}
