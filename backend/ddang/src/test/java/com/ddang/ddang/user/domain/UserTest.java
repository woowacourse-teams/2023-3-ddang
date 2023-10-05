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
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(new Reliability(5.0d))
                              .oauthId("12345")
                              .build();

        // when
        user.withdrawal();

        // then
        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    void 신뢰도_평균을_계산한다() {
        // when
        평가_대상.updateReliability(평가_대상이_받은_평가_목록);

        // then
        assertThat(평가_대상.getReliability().getValue()).isEqualTo(평가_대상의_신뢰도_점수);
    }
}
