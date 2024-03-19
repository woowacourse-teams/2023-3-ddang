package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.dto.ReadMessageDto;
import com.ddang.ddang.chat.application.event.UpdateReadMessageLogEvent;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.MessageNotFoundException;
import com.ddang.ddang.chat.application.fixture.MessageServiceFixture;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;

@IsolateDatabase
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MessageServiceTest extends MessageServiceFixture {

    @Autowired
    MessageService messageService;

    @Autowired
    ReadMessageLogRepository readMessageLogRepository;

    @MockBean
    LastReadMessageLogService lastReadMessageLogService;

    @Autowired
    ApplicationEvents events;

    @Test
    void 메시지를_생성한다() {
        // when
        final Long messageId = messageService.create(메시지_생성_DTO, 이미지_절대_경로);

        // then
        assertThat(messageId).isPositive();
    }

    @Test
    void 채팅방이_없는_경우_메시지를_생성하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> messageService.create(유효하지_않은_채팅방의_메시지_생성_DTO, 이미지_절대_경로))
                .isInstanceOf(ChatRoomNotFoundException.class)
                .hasMessageContaining("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");
    }

    @Test
    void 발신자가_없는_경우_메시지를_생성하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> messageService.create(유효하지_않은_발신자의_메시지_생성_DTO, 이미지_절대_경로))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("지정한 아이디에 대한 발신자를 찾을 수 없습니다.");
    }

    @Test
    void 수신자가_없는_경우_메시지를_생성하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> messageService.create(유효하지_않은_수신자의_메시지_생성_DTO, 이미지_절대_경로))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("지정한 아이디에 대한 수신자를 찾을 수 없습니다.");
    }

    @Test
    void 마지막_조회_메시지가_없는_경우_모든_메시지를_조회한다() {
        // given
        willDoNothing().given(lastReadMessageLogService).update(any());

        // when
        final List<ReadMessageDto> actual = messageService.readAllByLastMessageId(마지막_조회_메시지_아이디가_없는_메시지_조회용_request);

        // then
        assertThat(actual).hasSize(메시지_총_개수);
    }

    @Test
    void 첫_번째_메시지_이후에_생성된_모든_메시지를_조회한다() {
        // given
        willDoNothing().given(lastReadMessageLogService).update(any());

        // when
        final List<ReadMessageDto> actual = messageService.readAllByLastMessageId(두_번째_메시지부터_모든_메시지_조회용_request);

        // then
        assertThat(actual).hasSize(메시지_총_개수 - 1);
    }

    @Test
    void 마지막으로_조회된_메시지_이후에_추가된_메시지가_없는_경우_빈_리스트를_반환한다() {
        // when
        final List<ReadMessageDto> readMessageDtos = messageService.readAllByLastMessageId(
                조회할_메시지가_더이상_없는_메시지_조회용_request);

        // then
        assertThat(readMessageDtos).isEmpty();
    }

    @Test
    void 메시지를_조회할_경우_마지막으로_읽은_메시지_업데이트_이벤트를_호출한다() {
        // when
        messageService.readAllByLastMessageId(조회한_마지막_메시지가_5인_메시지_조회용_request);
        final long actual = events.stream(UpdateReadMessageLogEvent.class).count();

        // then
        assertThat(actual).isEqualTo(1);
    }

    @Test
    void 잘못된_사용자가_메시지를_조회할_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> messageService.readAllByLastMessageId(유효하지_않은_사용자의_메시지_조회용_request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("지정한 아이디에 대한 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 조회한_채팅방이_없는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> messageService.readAllByLastMessageId(유효하지_않은_채팅방의_메시지_조회용_request))
                .isInstanceOf(ChatRoomNotFoundException.class)
                .hasMessageContaining("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");
    }

    @Test
    void 조회한_마지막_메시지가_없는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> messageService.readAllByLastMessageId(존재하지_않는_마지막_메시지_아이디의_메시지_조회용_request))
                .isInstanceOf(MessageNotFoundException.class)
                .hasMessageContaining("조회한 마지막 메시지가 존재하지 않습니다.");
    }
}
