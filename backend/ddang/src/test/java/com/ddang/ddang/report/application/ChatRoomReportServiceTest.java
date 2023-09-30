package com.ddang.ddang.report.application;

import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.report.application.dto.CreateChatRoomReportDto;
import com.ddang.ddang.report.application.dto.ReadChatRoomReportDto;
import com.ddang.ddang.report.application.exception.AlreadyReportChatRoomException;
import com.ddang.ddang.report.application.exception.InvalidChatRoomReportException;
import com.ddang.ddang.report.application.fixture.ChatRoomReportServiceFixture;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatRoomReportServiceTest extends ChatRoomReportServiceFixture {

    @Autowired
    ChatRoomReportService chatRoomReportService;

    @Test
    void 채팅방_신고를_등록한다() {
        // given
        final CreateChatRoomReportDto createChatRoomReportDto = new CreateChatRoomReportDto(
                채팅방1.getId(),
                "신고합니다.",
                판매자겸_아직_신고하지_않은_신고자.getId()
        );

        // when
        final Long actual = chatRoomReportService.create(createChatRoomReportDto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_사용자가_채팅방을_신고할시_예외가_발생한다() {
        // given
        final CreateChatRoomReportDto createChatRoomReportDto = new CreateChatRoomReportDto(
                채팅방1.getId(),
                "신고합니다.",
                존재하지_않는_사용자_아이디
        );

        // when & then
        assertThatThrownBy(() -> chatRoomReportService.create(createChatRoomReportDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_채팅방을_신고할시_예외가_발생한다() {
        // given
        final CreateChatRoomReportDto createChatRoomReportDto = new CreateChatRoomReportDto(
                존재하지_않는_채팅방_아이디,
                "신고합니다.",
                판매자겸_아직_신고하지_않은_신고자.getId()
        );

        // when & then
        assertThatThrownBy(() -> chatRoomReportService.create(createChatRoomReportDto))
                .isInstanceOf(ChatRoomNotFoundException.class)
                .hasMessage("해당 채팅방을 찾을 수 없습니다.");
    }

    @Test
    void 판매자와_구매자_외의_사용자가_채팅방을_신고할시_예외가_발생한다() {
        // given
        final CreateChatRoomReportDto createChatRoomReportDto = new CreateChatRoomReportDto(
                채팅방1.getId(),
                "신고합니다.",
                채팅방_참여자가_아닌_사용자.getId()
        );

        // when & then
        assertThatThrownBy(() -> chatRoomReportService.create(createChatRoomReportDto))
                .isInstanceOf(InvalidChatRoomReportException.class)
                .hasMessage("해당 채팅방을 신고할 권한이 없습니다.");
    }

    @Test
    void 이미_신고한_채팅방을_동일_사용자가_신고하는_경우_예외가_발생한다() {
        // given
        final CreateChatRoomReportDto createChatRoomReportDto = new CreateChatRoomReportDto(
                채팅방1.getId(),
                "신고합니다.",
                이미_신고한_구매자1.getId()
        );

        // when & then
        assertThatThrownBy(() -> chatRoomReportService.create(createChatRoomReportDto))
                .isInstanceOf(AlreadyReportChatRoomException.class)
                .hasMessage("이미 신고한 채팅방입니다.");
    }

    @Test
    void 전체_신고_목록을_조회한다() {
        // when
        final List<ReadChatRoomReportDto> actual = chatRoomReportService.readAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0).reporterDto().id()).isEqualTo(이미_신고한_구매자1.getId());
            softAssertions.assertThat(actual.get(0).chatRoomDto().id()).isEqualTo(채팅방1.getId());
            softAssertions.assertThat(actual.get(1).reporterDto().id()).isEqualTo(이미_신고한_구매자2.getId());
            softAssertions.assertThat(actual.get(1).chatRoomDto().id()).isEqualTo(채팅방2.getId());
            softAssertions.assertThat(actual.get(2).reporterDto().id()).isEqualTo(이미_신고한_구매자3.getId());
            softAssertions.assertThat(actual.get(2).chatRoomDto().id()).isEqualTo(채팅방3.getId());
        });
    }
}
