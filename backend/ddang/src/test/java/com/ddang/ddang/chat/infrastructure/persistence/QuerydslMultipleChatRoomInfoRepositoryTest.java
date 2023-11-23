package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ReadMessageLog;
import com.ddang.ddang.chat.domain.dto.MultipleChatRoomInfoDto;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import com.ddang.ddang.chat.infrastructure.persistence.fixture.QuerydslMultipleChatRoomInfoRepositoryFixture;
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
class QuerydslMultipleChatRoomInfoRepositoryTest extends QuerydslMultipleChatRoomInfoRepositoryFixture {

    QuerydslMultipleChatRoomInfoRepository querydslMultipleChatRoomInfoRepository;

    ReadMessageLogRepository readMessageLogRepository;

    MessageRepository messageRepository;

    @BeforeEach
    void setUp(
            @Autowired final JPAQueryFactory queryFactory,
            @Autowired final JpaReadMessageLogRepository jpaReadMessageLogRepository,
            @Autowired final JpaMessageRepository jpaMessageRepository
    ) {
        querydslMultipleChatRoomInfoRepository = new QuerydslMultipleChatRoomInfoRepository(queryFactory);
        readMessageLogRepository = new ReadMessageLogRepositoryImpl(jpaReadMessageLogRepository);
        messageRepository = new MessageRepositoryImpl(jpaMessageRepository, new QuerydslMessageRepository(queryFactory));
    }

    @Test
    void 사용자가_읽지_않은_메시지_개수를_반환한다() {
        // given
        readMessageLogRepository.saveAll(List.of(new ReadMessageLog(메리_엔초_채팅방, 메리), new ReadMessageLog(메리_엔초_채팅방, 엔초)));

        // when
        messageRepository.save(메리가_엔초에게_3시에_보낸_쪽지1);
        messageRepository.save(메리가_엔초에게_3시에_보낸_쪽지2);
        messageRepository.save(메리가_엔초에게_3시에_보낸_쪽지3);
        messageRepository.save(엔초가_메리에게_3시에_보낸_쪽지);
        final List<MultipleChatRoomInfoDto> actual_엔초 = querydslMultipleChatRoomInfoRepository
                .findAllChatRoomInfoByUserIdOrderByLastMessage(엔초.getId());
        final List<MultipleChatRoomInfoDto> actual_메리 = querydslMultipleChatRoomInfoRepository
                .findAllChatRoomInfoByUserIdOrderByLastMessage(메리.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual_엔초.get(0).unreadMessageCount()).isEqualTo(3);
            softAssertions.assertThat(actual_메리.get(0).unreadMessageCount()).isEqualTo(1);
        });
    }

    @Test
    void 지정한_사용자_아이디가_포함된_채팅방을_조회한다() {
        // when
        final List<MultipleChatRoomInfoDto> actual =
                querydslMultipleChatRoomInfoRepository.findAllChatRoomInfoByUserIdOrderByLastMessage(엔초.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).chatRoom()).isEqualTo(엔초_지토_채팅방);
            softAssertions.assertThat(actual.get(0).message()).isEqualTo(엔초가_지토에게_5시에_보낸_쪽지);
            softAssertions.assertThat(actual.get(0).chatRoom().getAuction().getThumbnailImageStoreName()).isEqualTo(엔초의_경매_대표_이미지.getStoreName());
            softAssertions.assertThat(actual.get(1).chatRoom()).isEqualTo(제이미_엔초_채팅방);
            softAssertions.assertThat(actual.get(1).message()).isEqualTo(제이미가_엔초에게_4시에_보낸_쪽지);
            softAssertions.assertThat(actual.get(1).chatRoom().getAuction().getThumbnailImageStoreName()).isEqualTo(제이미의_경매_대표_이미지.getStoreName());
        });
    }
}
