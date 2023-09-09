package com.ddang.ddang.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.image.domain.Image;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserTest {

    @Test
    void 회원_정보를_수정한다() {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new Image("upload.png", "store.png"))
                              .reliability(5.0d)
                              .build();

        // when
        user.update("newName", new Image("newUpload.png", "newStore.png"));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(user.getName()).isEqualTo("newName");
            softAssertions.assertThat(user.getProfileImage()).isEqualTo(new Image("newUpload.png", "newStore.png"));
            softAssertions.assertThat(user.getReliability()).isEqualTo(5.0d);
        });
    }

    @Test
    void 회원_탈퇴한다() {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .build();

        // when
        user.withdrawal();

        // then
        assertThat(user.isDeleted()).isTrue();
    }
}
