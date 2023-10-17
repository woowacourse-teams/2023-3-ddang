package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.exception.ReadMessageLogNotFoundException;
import com.ddang.ddang.chat.application.fixture.LastReadMessageLogServiceFixture;
import com.ddang.ddang.chat.domain.ReadMessageLog;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LastReadMessageLogServiceTest extends LastReadMessageLogServiceFixture {

    @Autowired
    LastReadMessageLogService lastReadMessageLogService;

    @Autowired
    ReadMessageLogRepository readMessageLogRepository;

    @Test
    void 메시지_로그를_생성한다() {
        // given & when
        lastReadMessageLogService.create(메시지_로그_생성용_이벤트);
        final Optional<ReadMessageLog> actual = readMessageLogRepository.findBy(메시지_로그_생성용_발신자_겸_판매자.getId(), 메시지_로그_생성용_채팅방.getId());

        // then
        assertThat(actual).isPresent();
    }

    @Test
    void 메시지_로그를_업데이트한다() {
        // given & when
        lastReadMessageLogService.update(메시지_로그_업데이트용_이벤트);

        // then
        assertThat(메시지_로그_업데이트용_이벤트.lastReadMessage()).isEqualTo(메시지_로그_업데이트용_마지막_조회_메시지);
    }

    @Test
    void 메시지_로그_업데이트시_메시지_조회_로그가_존재하지_않으면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> lastReadMessageLogService.update(유효하지_않는_메시지_조회_로그))
                .isInstanceOf(ReadMessageLogNotFoundException.class);
    }
}
