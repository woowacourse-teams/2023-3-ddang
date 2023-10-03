package com.ddang.ddang.chat.domain;

import com.ddang.ddang.chat.domain.fixture.ChatRoomFixture;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatRoomTest extends ChatRoomFixture {

    @ParameterizedTest
    @CsvSource(value = {"0:true", "9:true", "10:false"}, delimiter = ':')
    void 채팅방_비활성화_여부를_체크한다(final long plusDay, final boolean expected) {
        // given
        final ChatRoom chatRoom = new ChatRoom(경매, 구매자);
        ReflectionTestUtils.setField(chatRoom, "createdTime", LocalDateTime.now());

        // when
        final boolean actual = chatRoom.isChatAvailableTime(chatRoom.getCreatedTime().plusDays(plusDay));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주어진_사용자의_채팅상대를_반환한다() {
        // given
        final ChatRoom chatRoom = new ChatRoom(경매, 구매자);

        // when
        final User actual = chatRoom.calculateChatPartnerOf(구매자);

        // then
        assertThat(actual).isEqualTo(판매자);
    }

    @Test
    void 주어진_사용자가_판매자라면_채팅_참여자이다() {
        // given
        final ChatRoom chatRoom = new ChatRoom(경매, 구매자);

        // when
        final boolean actual = chatRoom.isParticipant(판매자);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 주어진_사용자가_구매자라면_채팅_참여자이다() {
        // given
        final ChatRoom chatRoom = new ChatRoom(경매, 구매자);

        // when
        final boolean actual = chatRoom.isParticipant(구매자);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 주어진_사용자가_판매자와_구매자_모두_아니라면_채팅_참여자가_아니다() {
        // given
        final ChatRoom chatRoom = new ChatRoom(경매, 구매자);

        // when
        final boolean actual = chatRoom.isParticipant(경매에_참여하지_않는_사용자);

        // then
        assertThat(actual).isFalse();
    }
}
