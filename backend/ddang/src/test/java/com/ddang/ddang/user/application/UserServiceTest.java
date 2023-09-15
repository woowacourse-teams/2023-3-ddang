package com.ddang.ddang.user.application;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.application.dto.UpdateUserDto;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    JpaUserRepository userRepository;


    @MockBean
    StoreImageProcessor imageProcessor;

    @Test
    void 특정_사용자_정보를_조회한다() {
        // given
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        // when
        final ReadUserDto actual = userService.readById(user.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.name()).isEqualTo(user.getName());
            softAssertions.assertThat(actual.profileImageId()).isEqualTo(user.getProfileImage().getId());
            softAssertions.assertThat(actual.reliability()).isEqualTo(user.getReliability());
        });
    }

    @Test
    void 존재하지_않는_사용자_정보_조회시_예외를_반환한다() {
        // given
        final Long invalidUserId = -999L;

        // when & then
        assertThatThrownBy(() -> userService.readById(invalidUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("사용자 정보를 사용할 수 없습니다.");
    }

    @Test
    void 사용자_정보를_수정한다() {
        // given
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        final StoreImageDto storeImageDto = new StoreImageDto("newUpload.png", "newStore.png");
        given(imageProcessor.storeImageFile(any())).willReturn(storeImageDto);

        final MockMultipartFile updateImage = new MockMultipartFile(
                "updateImage.png",
                "updateImage.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1}
        );

        // when
        final ReadUserDto updateName = userService.updateById(
                user.getId(),
                new UpdateUserDto("updateName", updateImage)
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(user.getName()).isEqualTo("updateName");
            softAssertions.assertThat(user.getProfileImage().getImage().getStoreName()).isEqualTo("newStore.png");
            softAssertions.assertThat(user.getReliability()).isEqualTo(4.7d);
            softAssertions.assertThat(user.getOauthId()).isEqualTo("12345");
        });
    }

    @Test
    void 사용자_정보_수정시_존재하지_않는_사용자라면_예외가_발생한다() {
        // given
        final Long invalidUserId = -999L;

        final MockMultipartFile updateImage = new MockMultipartFile(
                "updateImage.png",
                "updateImage.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1}
        );

        // when & then
        assertThatThrownBy(() -> userService.updateById(invalidUserId, new UpdateUserDto("updateName", updateImage)))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("사용자 정보를 사용할 수 없습니다.");
    }

    @Test
    void 회원_탈퇴한다() {
        // given
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        // when
        userService.deleteById(user.getId());

        // then
        final Optional<User> actual = userRepository.findById(user.getId());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().isDeleted()).isTrue();
        });
    }

    @Test
    void 회원_탈퇴할때_이미_탈퇴한_회원이면_예외가_발생한다() {
        // given
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        user.withdrawal();
        userRepository.save(user);

        // when & then
        assertThatThrownBy(() -> userService.deleteById(user.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("사용자 정보를 사용할 수 없습니다.");
    }

    @Test
    void 회원_탈퇴할때_존재하지_않는_사용자_정보_조회시_예외를_반환한다() {
        // given
        final Long invalidUserId = -999L;

        // when & then
        assertThatThrownBy(() -> userService.deleteById(invalidUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("사용자 정보를 사용할 수 없습니다.");
    }
}
