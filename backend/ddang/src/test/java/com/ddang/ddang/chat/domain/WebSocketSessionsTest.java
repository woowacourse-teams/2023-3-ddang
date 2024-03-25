package com.ddang.ddang.chat.domain;

import com.ddang.ddang.chat.domain.fixture.WebSocketSessionsTestFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.WebSocketSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class WebSocketSessionsTest extends WebSocketSessionsTestFixture {

    WebSocketSessions sessions;

    WebSocketSession session;

    @BeforeEach
    void setUp() {
        sessions = new WebSocketSessions();
        session = mock(WebSocketSession.class);
    }

    @Test
    void 저장되지_않은_세션이라면_추가한다() {
        // given
        given(session.getAttributes()).willReturn(세션_attribute_정보);

        // when
        sessions.putIfAbsent(session, 채팅방_아이디);

        // then
        final boolean actual = sessions.getSessions().contains(session);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(sessions.getSessions()).hasSize(1);
            softAssertions.assertThat(actual).isTrue();
        });
    }

    @Test
    void 이미_저장된_세션이라면_추가하지_않는다() {
        // given
        given(session.getAttributes()).willReturn(세션_attribute_정보);
        sessions.getSessions().add(session);

        // when
        sessions.putIfAbsent(session, 채팅방_아이디);

        // then
        final boolean actual = sessions.getSessions().contains(session);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(sessions.getSessions()).hasSize(1);
            softAssertions.assertThat(actual).isTrue();
        });
    }

    @Test
    void 이미_저장된_세선이라면_참을_반환한다() {
        // given
        given(session.getAttributes()).willReturn(세션_attribute_정보);
        sessions.getSessions().add(session);

        // when
        final boolean actual = sessions.contains(사용자_아이디);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 저장되지_않은_세선이라면_거짓을_반환한다() {
        // given
        given(session.getAttributes()).willReturn(세션_attribute_정보);

        // when
        final boolean actual = sessions.contains(사용자_아이디);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 세션을_제거한다() {
        // given
        given(session.getAttributes()).willReturn(세션_attribute_정보);

        // when
        sessions.remove(session);

        // then
        final boolean actual = sessions.getSessions().contains(session);
        assertThat(actual).isFalse();
    }
}
