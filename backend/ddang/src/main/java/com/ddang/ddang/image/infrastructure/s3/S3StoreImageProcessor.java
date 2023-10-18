package com.ddang.ddang.image.infrastructure.s3;

import com.ddang.ddang.configuration.ProductProfile;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.image.infrastructure.local.exception.EmptyImageException;
import com.ddang.ddang.image.infrastructure.local.exception.StoreImageFailureException;
import com.ddang.ddang.image.infrastructure.local.exception.UnsupportedImageFileExtensionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@ProductProfile
@ConditionalOnProperty(name = "aws.s3.enabled", havingValue = "true")
@RequiredArgsConstructor
public class S3StoreImageProcessor implements StoreImageProcessor {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.image-path}")
    private String path;

    private final S3Client s3Client;

    @Override
    public List<StoreImageDto> storeImageFiles(final List<MultipartFile> imageFiles) {
        final List<StoreImageDto> storeImageDtos = new ArrayList<>();

        for (final MultipartFile imageFile : imageFiles) {
            if (imageFile.isEmpty()) {
                throw new EmptyImageException("이미지 파일의 데이터가 비어 있습니다.");
            }

            storeImageDtos.add(storeImageFile(imageFile));
        }

        return storeImageDtos;
    }

    @Override
    public StoreImageDto storeImageFile(final MultipartFile imageFile) {
        try {
            final String originalImageFileName = imageFile.getOriginalFilename();
            final String storeImageFileName = createStoreImageFileName(originalImageFileName);
            final String fullPath = findFullPath(storeImageFileName);
            final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                      .key(fullPath)
                                                                      .bucket(bucketName)
                                                                      .contentType(imageFile.getContentType())
                                                                      .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(imageFile.getInputStream(), imageFile.getSize())
            );

            return new StoreImageDto(originalImageFileName, storeImageFileName);
        } catch (final IOException ex) {
            throw new StoreImageFailureException("이미지 저장에 실패했습니다.", ex);
        } catch (final SdkException ex) {
            throw new StoreImageFailureException("AWS 이미지 저장에 실패했습니다.", ex);
        }
    }

    private String findFullPath(final String storeImageFileName) {
        return path + storeImageFileName;
    }

    private String createStoreImageFileName(final String originalFilename) {
        final String extension = extractExtension(originalFilename);

        validateImageFileExtension(extension);

        final String uuid = UUID.randomUUID().toString();

        return uuid + EXTENSION_FILE_CHARACTER + extension;
    }

    private String extractExtension(final String originalFilename) {
        int position = originalFilename.lastIndexOf(EXTENSION_FILE_CHARACTER);

        return originalFilename.substring(position + 1);
    }

    private void validateImageFileExtension(final String extension) {
        if (!WHITE_IMAGE_EXTENSION.contains(extension)) {
            throw new UnsupportedImageFileExtensionException("지원하지 않는 확장자입니다. : " + extension);
        }
    }
}
