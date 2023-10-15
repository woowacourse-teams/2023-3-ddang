package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.fixture.JpaChatRoomRepositoryFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
class JpaChatRoomRepositoryTest extends JpaChatRoomRepositoryFixture {

    @Autowired
    JpaChatRoomRepository jpaChatRoomRepository;

    @Test
    void 채팅방을_저장한다() {
        // given
        final ChatRoom chatRoom = new ChatRoom(경매, 구매자);

        // when
        jpaChatRoomRepository.save(chatRoom);

        // then
        assertThat(chatRoom.getId()).isPositive();
    }

    @Test
    void 지정한_아이디에_대한_채팅방을_조회한다() {
        // when
        final Optional<ChatRoom> actual = jpaChatRoomRepository.findById(채팅방.getId());

        // then
        assertThat(actual).contains(채팅방);
    }

    @Test
    void 지정한_경매_아이디가_포함된_채팅방의_아이디를_조회한다() {
        // when
        final Optional<Long> actual = jpaChatRoomRepository.findChatRoomIdByAuctionId(경매.getId());

        // then
        assertThat(actual).contains(채팅방.getId());
    }

    @Test
    void 지정한_경매_아이디가_포함된_채팅방이_존재한다면_참을_반환한다() {
        // when
        final boolean actual = jpaChatRoomRepository.existsByAuctionId(경매.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 지정한_경매_아이디가_포함된_채팅방이_존재하지_않는다면_거짓을_반환한다() {
        // when
        final boolean actual = jpaChatRoomRepository.existsByAuctionId(존재하지_않는_채팅방_아이디);

        // then
        assertThat(actual).isFalse();
    }
}
