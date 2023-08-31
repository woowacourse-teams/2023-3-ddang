package com.ddang.ddang.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SlackAppenderTest {

    @Test
    void 에러로그_발생시_슬랙_알림_메서드_호출_검증한다() {
        final SlackAppender slackAppender = mock(SlackAppender.class);

        willCallRealMethod().given(slackAppender)
                            .doAppend(any(ILoggingEvent.class));

        ch.qos.logback.classic.Logger rootLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(slackAppender);

        org.slf4j.Logger logger = LoggerFactory.getLogger(SlackAppenderTest.class);
        logger.error("appender test");

        verify(slackAppender).doAppend(ArgumentCaptor.forClass(ILoggingEvent.class)
                                                     .capture());
    }
}
