package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.fixture.QuerydslMessageRepositoryImplFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class QuerydslMessageRepositoryImplTest extends QuerydslMessageRepositoryImplFixture {

    QuerydslMessageRepositoryImpl querydslMessageRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory queryFactory) {
        querydslMessageRepository = new QuerydslMessageRepositoryImpl(queryFactory);
    }

    @Test
    void 마지막으로_읽은_메시지_이후에_추가된_메시지를_조회한다() {
        // when
        final List<Message> actual = querydslMessageRepository.findMessagesAllByLastMessageId(
                판매자.getId(),
                채팅방.getId(),
                세_번째_메시지.getId()
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(7);
            softAssertions.assertThat(actual.get(0)).isEqualTo(네_번째_메시지);
            softAssertions.assertThat(actual.get(1)).isEqualTo(다섯_번째_메시지);
            softAssertions.assertThat(actual.get(2)).isEqualTo(여섯_번째_메시지);
            softAssertions.assertThat(actual.get(3)).isEqualTo(일곱_번째_메시지);
            softAssertions.assertThat(actual.get(4)).isEqualTo(여덟_번째_메시지);
            softAssertions.assertThat(actual.get(5)).isEqualTo(아홉_번째_메시지);
            softAssertions.assertThat(actual.get(6)).isEqualTo(열_번째_메시지);
        });
    }

    @Test
    void 상대방이_메시지를_추가한_경우_마지막으로_읽은_메시지_이후의_메시지를_조회한다() {
        // when
        final List<Message> actual = querydslMessageRepository.findMessagesAllByLastMessageId(
                구매자.getId(),
                채팅방.getId(),
                세_번째_메시지.getId()
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(7);
            softAssertions.assertThat(actual.get(0)).isEqualTo(네_번째_메시지);
            softAssertions.assertThat(actual.get(1)).isEqualTo(다섯_번째_메시지);
            softAssertions.assertThat(actual.get(2)).isEqualTo(여섯_번째_메시지);
            softAssertions.assertThat(actual.get(3)).isEqualTo(일곱_번째_메시지);
            softAssertions.assertThat(actual.get(4)).isEqualTo(여덟_번째_메시지);
            softAssertions.assertThat(actual.get(5)).isEqualTo(아홉_번째_메시지);
            softAssertions.assertThat(actual.get(6)).isEqualTo(열_번째_메시지);
        });
    }

    @Test
    void 마지막으로_읽은_메시지_이후의_메시지가_없는_경우_빈_리스트를_반환한다() {
        // when
        final List<Message> actual = querydslMessageRepository.findMessagesAllByLastMessageId(
                구매자.getId(),
                채팅방.getId(),
                열_번째_메시지.getId()
        );

        // then
        assertThat(actual).hasSize(0);
    }
}
