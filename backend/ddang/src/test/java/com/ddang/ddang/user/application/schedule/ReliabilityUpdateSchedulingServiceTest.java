package com.ddang.ddang.user.application.schedule;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.user.application.schedule.fixture.ReliabilityUpdateSchedulingServiceFixture;
import com.ddang.ddang.user.domain.ReliabilityUpdateHistory;
import com.ddang.ddang.user.domain.UserReliability;
import com.ddang.ddang.user.infrastructure.persistence.JpaReliabilityUpdateHistoryRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserReliabilityRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReliabilityUpdateSchedulingServiceTest extends ReliabilityUpdateSchedulingServiceFixture {

    @Autowired
    ReliabilityUpdateSchedulingService reliabilityUpdateSchedulingService;

    @Autowired
    JpaReliabilityUpdateHistoryRepository reliabilityUpdateHistoryRepository;

    @Autowired
    JpaUserReliabilityRepository userReliabilityRepository;

    @Test
    void 사용자의_신뢰도를_갱신한다() {
        // when
        reliabilityUpdateSchedulingService.updateAllUserReliability();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(구매자1_기존_평가_2개_새로운_평가_1개.getReliability()).isEqualTo(구매자1_최종_신뢰도);
            softAssertions.assertThat(구매자2_기존_평가_3개_새로운_평가_2개.getReliability()).isEqualTo(구매자2_최종_신뢰도);
            softAssertions.assertThat(구매자3_기존_평가_5개_새로운_평가_1개.getReliability()).isEqualTo(구매자3_최종_신뢰도);
            softAssertions.assertThat(구매자4_기존_평가_없고_새로운_평가_1개.getReliability()).isEqualTo(구매자4_최종_신뢰도);
            softAssertions.assertThat(구매자5_기존_평가_없고_새로운_평가_3개.getReliability()).isEqualTo(구매자5_최종_신뢰도);
        });
    }

    @Test
    void 사용자의_신뢰도를_갱신하면_사용자_신뢰도_반영_정보도_갱신된다() {
        // when
        reliabilityUpdateSchedulingService.updateAllUserReliability();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            final Optional<UserReliability> actual1 = userReliabilityRepository.findByUserId(구매자1_기존_평가_2개_새로운_평가_1개.getId());
            softAssertions.assertThat(actual1.get().getAppliedReviewCount()).isEqualTo(구매자1_최종_신뢰도_반영_개수);
            final Optional<UserReliability> actual2 = userReliabilityRepository.findByUserId(구매자2_기존_평가_3개_새로운_평가_2개.getId());
            softAssertions.assertThat(actual2.get().getAppliedReviewCount()).isEqualTo(구매자2_최종_신뢰도_반영_개수);
            final Optional<UserReliability> actual3 = userReliabilityRepository.findByUserId(구매자3_기존_평가_5개_새로운_평가_1개.getId());
            softAssertions.assertThat(actual3.get().getAppliedReviewCount()).isEqualTo(구매자3_최종_신뢰도_반영_개수);
            final Optional<UserReliability> actual4 = userReliabilityRepository.findByUserId(구매자4_기존_평가_없고_새로운_평가_1개.getId());
            softAssertions.assertThat(actual4.get().getAppliedReviewCount()).isEqualTo(구매자4_최종_신뢰도_반영_개수);
            final Optional<UserReliability> actual5 = userReliabilityRepository.findByUserId(구매자5_기존_평가_없고_새로운_평가_3개.getId());
            softAssertions.assertThat(actual5.get().getAppliedReviewCount()).isEqualTo(구매자5_최종_신뢰도_반영_개수);

            final Optional<UserReliability> actual6 = userReliabilityRepository.findByUserId(구매자6_기존_평가_없고_새로운_평가_없음.getId());
            softAssertions.assertThat(actual6).isEmpty();
        });
    }

    @Test
    void 사용자_신뢰도_갱신_후에_마지막으로_적용된_평가의_아이디를_디비에_저장한다() {
        // when
        reliabilityUpdateSchedulingService.updateAllUserReliability();

        // then
        final Optional<ReliabilityUpdateHistory> actual = reliabilityUpdateHistoryRepository.findFirstByOrderByIdDesc();

        assertThat(actual.get().getLastAppliedReviewId()).isEqualTo(새로운_평가를_업데이트한_후의_마지막으로_적용된_평가의_아이디);
    }
}
