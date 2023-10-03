package com.ddang.ddang.user.application;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.application.fixture.UserServiceFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserServiceTest extends UserServiceFixture {

    @Autowired
    UserService userService;

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
        given(imageProcessor.storeImageFile(any())).willReturn(새로운_프로필_이미지_dto);

        // when
        userService.updateById(사용자.getId(), 사용자_정보_수정_요청_dto);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(사용자.getName()).isEqualTo(사용자_정보_수정_요청_dto.name());
            softAssertions.assertThat(사용자.getProfileImage().getImage().getStoreName())
                          .isEqualTo(새로운_프로필_이미지_dto.storeName());
            softAssertions.assertThat(사용자.getProfileImage().getImage().getUploadName())
                          .isEqualTo(새로운_프로필_이미지_dto.uploadName());
        });
    }

    @Test
    void 사용자_정보를_수정시_이름만_수정한다() {
        // when
        userService.updateById(사용자.getId(), 사용자_이름만_수정_요청_dto);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(사용자.getName()).isEqualTo(사용자_이름만_수정_요청_dto.name());
            softAssertions.assertThat(사용자.getProfileImage()).isEqualTo(프로필_이미지);
        });
    }

    @Test
    void 사용자_정보를_수정시_이미지만_수정한다() {
        // given
        given(imageProcessor.storeImageFile(any())).willReturn(새로운_프로필_이미지_dto);

        // when
        userService.updateById(사용자.getId(), 사용자_이미지만_수정_요청_dto);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(사용자.getName()).isEqualTo(사용자_이름);
            softAssertions.assertThat(사용자.getProfileImage().getImage().getStoreName())
                          .isEqualTo(새로운_프로필_이미지_dto.storeName());
            softAssertions.assertThat(사용자.getProfileImage().getImage().getUploadName())
                          .isEqualTo(새로운_프로필_이미지_dto.uploadName());
        });
    }

    @Test
    void 사용자_정보_수정시_존재하지_않는_사용자라면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> userService.updateById(존재하지_않는_사용자_아이디, 사용자_정보_수정_요청_dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("사용자 정보를 사용할 수 없습니다.");
    }
}
