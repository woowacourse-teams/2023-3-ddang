package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.report.infrastructure.persistence.fixture.JpaChatRoomReportRepositoryFixture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaChatRoomReportRepositoryTest extends JpaChatRoomReportRepositoryFixture {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaChatRoomReportRepository chatRoomReportRepository;

    @Test
    void 채팅방_신고를_저장한다() {
        final ChatRoomReport chatRoomReport = new ChatRoomReport(구매자1, 채팅방1, "신고합니다.");

        // when
        final ChatRoomReport actual = chatRoomReportRepository.save(chatRoomReport);

        // then
        em.flush();
        em.clear();

        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 특정_채팅방_아이디와_신고자_아이디가_동일한_레코드가_존재한다면_참을_반환한다() {
        // when
        final boolean actual = chatRoomReportRepository.existsByChatRoomIdAndReporterId(채팅방1.getId(), 구매자1겸_신고자.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_채팅방_아이디와_신고자_아이디가_동일한_레코드가_존재하지_않는다면_거짓을_반환한다() {
        // when
        final boolean actual = chatRoomReportRepository.existsByChatRoomIdAndReporterId(
                존재하지_않는_채팅방_아이디,
                존재하지_않는_사용자_아이디
        );

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 전체_채팅방_신고_목록을_조회한다() {
        // when
        final List<ChatRoomReport> actual = chatRoomReportRepository.findAllByOrderByIdAsc();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).isEqualTo(채팅방_신고1);
            softAssertions.assertThat(actual.get(1)).isEqualTo(채팅방_신고2);
            softAssertions.assertThat(actual.get(2)).isEqualTo(채팅방_신고3);
        });
    }
}
