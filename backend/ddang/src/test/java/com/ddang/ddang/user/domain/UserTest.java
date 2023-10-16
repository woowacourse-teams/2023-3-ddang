package com.ddang.ddang.user.domain;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.fixture.UserFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserTest extends UserFixture {

    @Test
    void 회원_생성시_신뢰도가_null이면_신뢰도_점수가_double의_최솟값인_신뢰도로_회원을_생성한다() {
        // given
        final Reliability nullReliability = null;
        final Reliability expect = new Reliability(Double.MIN_VALUE);

        // when
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new ProfileImage("uplad.png", "store.png"))
                              .reliability(nullReliability)
                              .oauthId("12345")
                              .build();

        // then
        assertThat(user.getReliability()).isEqualTo(expect);
    }

    @Test
    void 회원_생성시_신뢰도가_null이_아니라면_해당_신뢰도로_회원을_생성한다() {
        // given
        final Reliability notNullReliability = new Reliability(4.5d);

        // when
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new ProfileImage("uplad.png", "store.png"))
                              .reliability(notNullReliability)
                              .oauthId("12345")
                              .build();

        // then
        assertThat(user.getReliability()).isEqualTo(notNullReliability);
    }

    @Test
    void 회원_정보의_이름을_수정한다() {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(new Reliability(5.0d))
                              .oauthId("12345")
                              .build();

        // when
        user.updateName("newName");

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(user.getName()).isEqualTo("newName");
            softAssertions.assertThat(user.getProfileImage()).isEqualTo(new ProfileImage("upload.png", "store.png"));
            softAssertions.assertThat(user.getReliability().getValue()).isEqualTo(5.0d);
        });
    }

    @Test
    void 회원_정보의_이미지를_수정한다() {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(new Reliability(5.0d))
                              .oauthId("12345")
                              .build();

        // when
        user.updateProfileImage(new ProfileImage("updateUpload.png", "updateStore.png"));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(user.getName()).isEqualTo("kakao12345");
            softAssertions.assertThat(user.getProfileImage()).isEqualTo(new ProfileImage("updateUpload.png", "updateStore.png"));
            softAssertions.assertThat(user.getReliability().getValue()).isEqualTo(5.0d);
        });
    }

    @Test
    void 회원_탈퇴한다() {
        // given
        final String userName = "kakao12345";
        final User user = User.builder()
                              .name(userName)
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(new Reliability(5.0d))
                              .oauthId("12345")
                              .build();

        // when
        user.withdrawal();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(user.isDeleted()).isTrue();
            softAssertions.assertThat(user.getName()).isNotEqualTo(userName);
            softAssertions.assertThat(user.getProfileImage()).isNull();
        });
    }

    @Test
    void 새로운_신뢰도로_변경한다() {
        // when
        평가_대상.updateReliability(새로운_신뢰도);

        // then
        assertThat(평가_대상.getReliability().getValue()).isEqualTo(새로운_신뢰도_점수);
    }
}
