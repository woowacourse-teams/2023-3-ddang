package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.fixture.LastReadMessageLogEventListenerFixture;
import com.ddang.ddang.configuration.IsolateDatabase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@IsolateDatabase
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LastReadMessageLogEventListenerTest extends LastReadMessageLogEventListenerFixture {

    @Autowired
    ApplicationEvents events;

    @Autowired
    LastReadMessageLogEventListener lastReadMessageLogEventListener;

    @MockBean
    LastReadMessageLogService lastReadMessageLogService;

    @Test
    void 이벤트가_호출되면_메시지_로그를_저장한다() {
        // given
        willDoNothing().given(lastReadMessageLogService).create(any());

        // when
        lastReadMessageLogEventListener.create(생성용_메시지_조회_로그);

        // then
        verify(lastReadMessageLogService).create(any());
    }

    @Test
    void 이벤트가_호출되면_메시지_로그를_업데이트한다() {
        // given
        willDoNothing().given(lastReadMessageLogService).update(any());

        // when
        lastReadMessageLogEventListener.update(업데이트용_메시지_조회_로그);

        // then
        verify(lastReadMessageLogService).update(any());
    }
}
