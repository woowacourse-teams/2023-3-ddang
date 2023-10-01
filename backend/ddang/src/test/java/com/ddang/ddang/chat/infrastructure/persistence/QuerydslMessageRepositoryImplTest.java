package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.fixture.QuerydslMessageRepositoryImplFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
        final Long lastMessageId = 3L;
        final List<Message> actual = querydslMessageRepository.findMessagesAllByLastMessageId(
                판매자.getId(),
                채팅방.getId(),
                lastMessageId
        );
        final int expected = (int) (메시지_총_개수 - lastMessageId);

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(expected);
    }

    @Test
    void 상대방이_메시지를_추가한_경우_마지막으로_읽은_메시지_이후의_메시지를_조회한다() {
        // when
        final Long lastMessageId = 3L;
        final List<Message> messages = querydslMessageRepository.findMessagesAllByLastMessageId(
                구매자.getId(),
                채팅방.getId(),
                lastMessageId
        );
        final int expected = (int) (메시지_총_개수 - lastMessageId);

        // then
        assertThat(messages).hasSizeGreaterThanOrEqualTo(expected);
    }
}
