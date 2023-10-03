package com.ddang.ddang.notification.application;

import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.event.MessageNotificationEvent;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.notification.application.fixture.NotificationEventListenerFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.assertj.core.api.Assertions.assertThat;

@IsolateDatabase
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NotificationEventListenerTest extends NotificationEventListenerFixture {

    @Autowired
    NotificationEventListener notificationEventListener;

    @Autowired
    ApplicationEvents events;

    @Autowired
    MessageService messageService;

    @Test
    void 메시지를_전송하면_알림을_전송한다() {
        // when
        messageService.create(메시지_생성_DTO, 이미지_절대_경로);

        // then
        final long actual = events.stream(MessageNotificationEvent.class).count();
        assertThat(actual).isEqualTo(1);
    }
}
