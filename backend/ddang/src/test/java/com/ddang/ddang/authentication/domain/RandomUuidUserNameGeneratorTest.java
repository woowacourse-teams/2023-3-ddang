package com.ddang.ddang.authentication.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RandomUuidUserNameGeneratorTest {

    @Test
    void 무작위_이름을_생성한다() {
        // given
        final UserNameGenerator generator = new RandomUuidUserNameGenerator();

        // when
        final String actual = generator.generate();

        // then
        assertThat(actual.length()).isSameAs(10);
    }
}
