package com.ddang.ddang.user.domain;

import com.ddang.ddang.user.domain.fixture.UserReliabilityFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class UserReliabilityTest extends UserReliabilityFixture {

    @Test
    void 새로운_평가가_주어지면_사용자의_신뢰도_관련_정보들을_갱신한다() {
        // given
        final UserReliability userReliability = new UserReliability(기존에_평가_3개_받은_평가_대상);
        ReflectionTestUtils.setField(userReliability, "appliedReviewCount", 3);

        // when
        userReliability.updateReliability(평가_대상이_받은_새로운_평가_목록);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(userReliability.getReliability()).isEqualTo(새로운_평가가_반영된_신뢰도);
            softAssertions.assertThat(userReliability.getAppliedReviewCount()).isEqualTo(새로운_평가_반영_이후의_평가_대상의_신뢰도에_반영된_평가_개수);
            softAssertions.assertThat(기존에_평가_3개_받은_평가_대상.getReliability()).isEqualTo(새로운_평가가_반영된_신뢰도);
        });
    }

    @Test
    void 신뢰도_기록이_없다면_신뢰도_점수는_그대로이다() {
        // given
        final UserReliability userReliability = new UserReliability(기존에_평가_3개_받은_평가_대상);
        ReflectionTestUtils.setField(userReliability, "appliedReviewCount", 3);

        // when
        userReliability.updateReliability(비어있는_평가_목록);

        // then

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(userReliability.getReliability()).isEqualTo(기존에_평가3개가_적용되어있는_신뢰도);
            softAssertions.assertThat(userReliability.getAppliedReviewCount()).isEqualTo(새로운_평가_반영_전의_평가_대상의_신뢰도에_반영된_평가_개수);
            softAssertions.assertThat(기존에_평가_3개_받은_평가_대상.getReliability()).isEqualTo(기존에_평가3개가_적용되어있는_신뢰도);
        });
    }
}
