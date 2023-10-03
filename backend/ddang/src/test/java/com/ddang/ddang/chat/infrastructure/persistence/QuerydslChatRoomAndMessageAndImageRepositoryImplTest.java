package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageAndImageDto;
import com.ddang.ddang.chat.infrastructure.persistence.fixture.QuerydslChatRoomAndMessageAndImageRepositoryImplFixture;
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

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuerydslChatRoomAndMessageAndImageRepositoryImplTest extends QuerydslChatRoomAndMessageAndImageRepositoryImplFixture {

    QuerydslChatRoomAndMessageAndImageRepository querydslChatRoomAndMessageAndImageRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory queryFactory) {
        querydslChatRoomAndMessageAndImageRepository = new QuerydslChatRoomAndMessageAndImageRepositoryImpl(queryFactory);
    }

    @Test
    void 지정한_사용자_아이디가_포함된_채팅방을_조회한다() {
        // when
        final List<ChatRoomAndMessageAndImageDto> actual =
                querydslChatRoomAndMessageAndImageRepository.findAllChatRoomInfoByUserIdOrderByLastMessage(엔초.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0).chatRoom()).isEqualTo(엔초_지토_채팅방);
            softAssertions.assertThat(actual.get(0).message()).isEqualTo(엔초가_지토에게_5시에_보낸_쪽지);
            softAssertions.assertThat(actual.get(0).thumbnailImage()).isEqualTo(엔초의_경매_대표_이미지);
            softAssertions.assertThat(actual.get(1).chatRoom()).isEqualTo(제이미_엔초_채팅방);
            softAssertions.assertThat(actual.get(1).message()).isEqualTo(제이미가_엔초에게_4시에_보낸_쪽지);
            softAssertions.assertThat(actual.get(1).thumbnailImage()).isEqualTo(제이미의_경매_대표_이미지);
            softAssertions.assertThat(actual.get(2).chatRoom()).isEqualTo(메리_엔초_채팅방);
            softAssertions.assertThat(actual.get(2).message()).isEqualTo(메리가_엔초에게_3시에_보낸_쪽지);
            softAssertions.assertThat(actual.get(2).thumbnailImage()).isEqualTo(메리의_경매_대표_이미지);
        });
    }
}
