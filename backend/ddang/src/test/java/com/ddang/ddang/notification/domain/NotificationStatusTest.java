package com.ddang.ddang.notification.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationStatusTest {

    @Test
    void 정상적인_토큰이_전달된_경우_알림_상태_성공을_반환한다() {
        // given
        final String fcmReturnedValue = "successToken";

        // when
        final NotificationStatus expected = NotificationStatus.calculateStatus(fcmReturnedValue);

        //then
        assertThat(expected).isEqualTo(NotificationStatus.SUCCESS);
    }

    @Test
    void null이_전달된_경우_알림_상태_실패를_반환한다() {
        // given
        final String fcmReturnedValue = null;

        // when
        final NotificationStatus expected = NotificationStatus.calculateStatus(fcmReturnedValue);

        //then
        assertThat(expected).isEqualTo(NotificationStatus.FAIL);
    }
}
