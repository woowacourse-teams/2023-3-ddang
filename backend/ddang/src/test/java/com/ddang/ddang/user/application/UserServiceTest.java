package com.ddang.ddang.user.application;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.application.dto.UpdateUserDto;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.application.fixture.UserServiceFixture;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserServiceTest extends UserServiceFixture {

    @Autowired
    UserService userService;

    @Autowired
    JpaUserRepository userRepository;

    @MockBean
    StoreImageProcessor imageProcessor;

    @Test
    void 특정_사용자_정보를_조회한다() {
        // when
        final ReadUserDto actual = userService.readById(사용자.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.name()).isEqualTo(사용자.getName());
            softAssertions.assertThat(actual.profileImageId()).isEqualTo(사용자.getProfileImage().getId());
            softAssertions.assertThat(actual.reliability()).isEqualTo(사용자.getReliability());
        });
    }

    @Test
    void 존재하지_않는_사용자_정보_조회시_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> userService.readById(존재하지_않는_사용자_아이디))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("사용자 정보를 사용할 수 없습니다.");
    }

    @Test
    void 사용자_정보를_수정한다() {
        // given
        final StoreImageDto storeImageDto = new StoreImageDto("newUpload.png", "newStore.png");
        given(imageProcessor.storeImageFile(any())).willReturn(storeImageDto);

        final UpdateUserDto updateUserDto = new UpdateUserDto("updateName", 프로필_이미지);

        // when
        userService.updateById(사용자.getId(), updateUserDto);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(사용자.getName()).isEqualTo("updateName");
            softAssertions.assertThat(사용자.getProfileImage().getImage().getStoreName()).isEqualTo("newStore.png");
            softAssertions.assertThat(사용자.getReliability()).isEqualTo(4.7d);
            softAssertions.assertThat(사용자.getOauthId()).isEqualTo("12345");
        });
    }

    @Test
    void 사용자_정보를_수정시_이름만_수정한다() {
        // given
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        final UpdateUserDto updateUserDto = new UpdateUserDto("updateName", null);

        // when
        userService.updateById(user.getId(), updateUserDto);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(user.getName()).isEqualTo("updateName");
            softAssertions.assertThat(user.getProfileImage().getImage().getStoreName()).isEqualTo("store.png");
            softAssertions.assertThat(user.getReliability()).isEqualTo(4.7d);
            softAssertions.assertThat(user.getOauthId()).isEqualTo("12345");
        });
    }

    @Test
    void 사용자_정보를_수정시_이미지만_수정한다() {
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

        final UpdateUserDto updateUserDto = new UpdateUserDto(null, updateImage);

        // when
        userService.updateById(user.getId(), updateUserDto);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(user.getName()).isEqualTo("사용자");
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
}
