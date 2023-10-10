package com.ddang.ddang.chat.domain;

import com.ddang.ddang.chat.domain.fixture.ChatRoomFixture;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatRoomTest extends ChatRoomFixture {

    @Test
    void 탈퇴하지_않은_사용자와는_채팅이_가능하다() {

        final ChatRoom chatRoom = new ChatRoom(경매, 구매자);

        // when
        final boolean actual = chatRoom.isChatAvailablePartner(구매자);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 탈퇴한_사용자와는_채팅이_불가능하다() {
        // given
        final ChatRoom chatRoom = new ChatRoom(경매, 탈퇴한_사용자);

        // when
        final boolean actual = chatRoom.isChatAvailablePartner(탈퇴한_사용자);

        // then
        assertThat(actual).isFalse();
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
