package com.ddang.ddang.websocket.handler;

import com.ddang.ddang.chat.handler.ChatWebSocketHandleTextMessageProvider;
import com.ddang.ddang.websocket.handler.dto.TextMessageType;
import com.ddang.ddang.websocket.handler.exception.UnsupportedTextMessageTypeException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class WebSocketHandleTextMessageProviderCompositeTest {

    @Test
    void 지원하는_웹소켓_메시지_타입을_전달하면_해당_웹소켓_메시지_핸들러_provider를_반환한다() {
        // given
        final ChatWebSocketHandleTextMessageProvider provider = new ChatWebSocketHandleTextMessageProvider(
                null,
                null,
                null
        );
        final WebSocketHandleTextMessageProviderComposite composite =
                new WebSocketHandleTextMessageProviderComposite(Set.of(provider));

        // when
        final WebSocketHandleTextMessageProvider actual = composite.findProvider(TextMessageType.CHATTINGS);

        // then
        assertThat(actual).isInstanceOf(ChatWebSocketHandleTextMessageProvider.class);
    }

    @Test
    void 지원하지_않는_웹소켓_메시지_타입을_전달하면_예외가_발생한다() {
        // given
        final WebSocketHandleTextMessageProviderComposite composite =
                new WebSocketHandleTextMessageProviderComposite(Set.of());

        // when & then
        assertThatThrownBy(() -> composite.findProvider(TextMessageType.CHATTINGS))
                .isInstanceOf(UnsupportedTextMessageTypeException.class)
                .hasMessage("지원하는 웹 소켓 통신 타입이 아닙니다.");
    }
}
