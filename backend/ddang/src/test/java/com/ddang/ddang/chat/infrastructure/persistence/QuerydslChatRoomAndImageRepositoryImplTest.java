package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndImageDto;
import com.ddang.ddang.chat.infrastructure.persistence.fixture.QuerydslChatRoomAndImageRepositoryImplFixture;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuerydslChatRoomAndImageRepositoryImplTest extends QuerydslChatRoomAndImageRepositoryImplFixture {

    QuerydslChatRoomAndImageRepository querydslChatRoomAndImageRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory queryFactory) {
        querydslChatRoomAndImageRepository = new QuerydslChatRoomAndImageRepositoryImpl(queryFactory);
    }

    @Test
    void 지정한_채팅방_아이디에_해당하는_채팅방을_조회한다() {
        // given
        final ChatRoomAndImageDto expect = new ChatRoomAndImageDto(채팅방, 경매_대표_이미지);

        // when
        final Optional<ChatRoomAndImageDto> actual = querydslChatRoomAndImageRepository.findChatRoomById(채팅방.getId());

        // then
        assertThat(actual).contains(expect);
    }
}
