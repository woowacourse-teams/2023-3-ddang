package com.ddang.ddang.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.fixture.UserFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserTest extends UserFixture {

    @Test
    void 회원_생성시_신뢰도가_null이면_신뢰도_점수가_음수인_신뢰도로_회원을_생성한다() {
        // given
        final Reliability nullReliability = null;

        // when
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new ProfileImage("uplad.png", "store.png"))
                              .reliability(nullReliability)
                              .oauthId("12345")
                              .build();

        // then
        assertThat(user.getReliability().getValue()).isLessThan(0.0d);
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
            softAssertions.assertThat(user.findName()).isEqualTo("newName");
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
            softAssertions.assertThat(user.findName()).isEqualTo("kakao12345");
            softAssertions.assertThat(user.getProfileImage())
                          .isEqualTo(new ProfileImage("updateUpload.png", "updateStore.png"));
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
            softAssertions.assertThat(user.findName()).isNotEqualTo(userName);
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

    @Test
    void 프로필_이미지를_조회할_때_이미지가_없다면_기본_이미지_파일_이름을_반환한다() {
        // when
        final String actual = 프로필_이미지가_없는_회원.getProfileImageStoreName();

        // then
        assertThat(actual).isEqualTo(ProfileImage.DEFAULT_PROFILE_IMAGE_STORE_NAME);
    }

    @Test
    void 프로필_이미지를_조회한다() {
        // when
        final String actual = 프로필_이미지가_있는_회원.getProfileImageStoreName();

        // then
        assertThat(actual).isEqualTo(프로필_이미지_파일_이름);
    }

    @Test
    void 탈퇴한_회원의_이름을_조회하면_알_수_없음을_반환한다() {
        // given
        final User user = User.builder()
                              .build();
        user.withdrawal();

        // when
        final String actual = user.findName();

        // then
        assertThat(actual).isEqualTo("알 수 없음");
    }

    @Test
    void 회원의_이름을_조회한다() {
        // given
        final User user = User.builder()
                              .name("이름")
                              .build();

        // when
        final String actual = user.findName();

        // then
        assertThat(actual).isEqualTo("이름");
    }

    @Test
    void 회원의_신뢰도가_초기화된_이후_변경되지_않았다면_null을_반환한다() {
        // given
        final User user = User.builder()
                              .name("이름")
                              .build();

        // when
        final Float actual = user.findReliability();

        // then
        assertThat(actual).isNull();
    }

    @Test
    void 회원의_신뢰도가_변경되었다면_신뢰도를_반환한다() {
        // given
        final User user = User.builder()
                              .reliability(new Reliability(5.0d))
                              .build();

        // when
        final Float actual = user.findReliability();

        // then
        assertThat(actual).isEqualTo(5.0f);
    }
}
