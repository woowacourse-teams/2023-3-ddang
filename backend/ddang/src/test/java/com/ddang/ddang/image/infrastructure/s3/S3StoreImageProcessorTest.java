package com.ddang.ddang.image.infrastructure.s3;

import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.image.infrastructure.local.exception.EmptyImageException;
import com.ddang.ddang.image.infrastructure.local.exception.StoreImageFailureException;
import com.ddang.ddang.image.infrastructure.local.exception.UnsupportedImageFileExtensionException;
import com.ddang.ddang.image.infrastructure.s3.fixture.S3StoreImageProcessorFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class S3StoreImageProcessorTest extends S3StoreImageProcessorFixture {

    @InjectMocks
    S3StoreImageProcessor imageProcessor;

    @Mock
    S3Client s3Client;

    @Test
    void 이미지_파일이_비어_있는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> imageProcessor.storeImageFiles(List.of(빈_이미지_파일)))
                .isInstanceOf(EmptyImageException.class)
                .hasMessage("이미지 파일의 데이터가 비어 있습니다.");
    }

    @Test
    void 허용되지_않은_확장자의_이미지_파일인_경우_예외가_발생한다() {
        // given
        given(이미지_파일.getOriginalFilename()).willReturn(지원하지_않는_확장자를_가진_이미지_파일명);

        // when & then
        assertThatThrownBy(() -> imageProcessor.storeImageFiles(List.of(이미지_파일)))
                .isInstanceOf(UnsupportedImageFileExtensionException.class)
                .hasMessageContaining("지원하지 않는 확장자입니다.");
    }

    @Test
    void 이미지_저장에_실패한_경우_예외가_발생한다() throws IOException {
        // given
        given(이미지_파일.getOriginalFilename()).willReturn(기존_이미지_파일명);
        given(이미지_파일.getInputStream()).willThrow(new IOException());

        // when & then
        assertThatThrownBy(() -> imageProcessor.storeImageFiles(List.of(이미지_파일)))
                .isInstanceOf(StoreImageFailureException.class)
                .hasMessage("이미지 저장에 실패했습니다.");
    }

    @Test
    void AWS_이미지_저장에_실패한_경우_예외가_발생한다() throws IOException {
        // given
        final ByteArrayInputStream fakeInputStream = new ByteArrayInputStream("가짜 이미지 데이터".getBytes());
        given(이미지_파일.getOriginalFilename()).willReturn(기존_이미지_파일명);
        given(이미지_파일.getInputStream()).willReturn(fakeInputStream);
        given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .willThrow(SdkException.class);

        // when & then
        assertThatThrownBy(() -> imageProcessor.storeImageFiles(List.of(이미지_파일)))
                .isInstanceOf(StoreImageFailureException.class)
                .hasMessage("AWS 이미지 저장에 실패했습니다.");
    }

    @Test
    void 유효한_이미지_파일인_경우_이미지_파일을_저장한다() throws Exception {
        // given
        final ByteArrayInputStream fakeInputStream = new ByteArrayInputStream("가짜 이미지 데이터".getBytes());
        given(이미지_파일.getOriginalFilename()).willReturn(기존_이미지_파일명);
        given(이미지_파일.getInputStream()).willReturn(fakeInputStream);
        given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .willReturn(PutObjectResponse.builder().build());

        // when
        final List<StoreImageDto> actual = imageProcessor.storeImageFiles(List.of(이미지_파일));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).storeName()).isNotBlank();
            softAssertions.assertThat(actual.get(0).uploadName()).isEqualTo(기존_이미지_파일명);
        });
    }
}
