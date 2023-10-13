package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoomAndImageDto;
import com.ddang.ddang.chat.infrastructure.persistence.fixture.QuerydslChatRoomAndImageRepositoryImplFixture;
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

import java.util.Optional;

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
        // when
        final Optional<ChatRoomAndImageDto> actual = querydslChatRoomAndImageRepository.findChatRoomById(채팅방.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().chatRoom()).isEqualTo(채팅방);
            softAssertions.assertThat(actual.get().thumbnailImage()).isEqualTo(경매_대표_이미지);
        });
    }
}
