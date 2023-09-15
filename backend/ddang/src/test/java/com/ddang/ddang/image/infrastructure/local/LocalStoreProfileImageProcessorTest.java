package com.ddang.ddang.image.infrastructure.local;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;

import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.image.infrastructure.local.exception.EmptyImageException;
import com.ddang.ddang.image.infrastructure.local.exception.StoreImageFailureException;
import com.ddang.ddang.image.infrastructure.local.exception.UnsupportedImageFileExtensionException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LocalStoreProfileImageProcessorTest {

    LocalStoreImageProcessor imageProcessor = new LocalStoreImageProcessor();

    @Test
    void 이미지_파일이_비어_있는_경우_예외가_발생한다() {
        // given
        final MockMultipartFile imageFile = new MockMultipartFile("image.png", new byte[0]);

        // when & then
        assertThatThrownBy(() -> imageProcessor.storeImageFiles(List.of(imageFile)))
                .isInstanceOf(EmptyImageException.class)
                .hasMessage("이미지 파일의 데이터가 비어 있습니다.");
    }

    @Test
    void 이미지_저장에_실패한_경우_예외가_발생한다() throws Exception {
        // given
        final MultipartFile imageFile = mock(MultipartFile.class);

        given(imageFile.getOriginalFilename()).willReturn("image.png");
        willThrow(IOException.class).given(imageFile).transferTo(any(File.class));

        // when & then
        assertThatThrownBy(() -> imageProcessor.storeImageFiles(List.of(imageFile)))
                .isInstanceOf(StoreImageFailureException.class)
                .hasMessage("이미지 저장에 실패했습니다.");
    }

    @Test
    void 허용되지_않은_확장자의_이미지_파일인_경우_예외가_발생한다() {
        // given
        final MultipartFile imageFile = mock(MultipartFile.class);

        given(imageFile.getOriginalFilename()).willReturn("image.gif");

        // when & then
        assertThatThrownBy(() -> imageProcessor.storeImageFiles(List.of(imageFile)))
                .isInstanceOf(UnsupportedImageFileExtensionException.class)
                .hasMessageContaining("지원하지 않는 확장자입니다.");
    }

    @Test
    void 유효한_이미지_파일인_경우_이미지_파일을_저장한다() throws Exception {
        // given
        final MultipartFile imageFile = mock(MultipartFile.class);
        final String imageFileName = "image.png";

        given(imageFile.getOriginalFilename()).willReturn(imageFileName);
        willDoNothing().given(imageFile).transferTo(any(File.class));

        // when
        final List<StoreImageDto> actual = imageProcessor.storeImageFiles(List.of(imageFile));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).storeName()).isNotBlank();
            softAssertions.assertThat(actual.get(0).uploadName()).isEqualTo(imageFileName);
        });
    }
}
