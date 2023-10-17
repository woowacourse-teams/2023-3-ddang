package com.ddang.ddang.image.infrastructure.local;

import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.image.infrastructure.local.exception.EmptyImageException;
import com.ddang.ddang.image.infrastructure.local.exception.StoreImageFailureException;
import com.ddang.ddang.image.infrastructure.local.exception.UnsupportedImageFileExtensionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class LocalStoreImageProcessor implements StoreImageProcessor {

    @Value("${image.store.dir}")
    private String imageStoreDir;

    @Override
    public List<StoreImageDto> storeImageFiles(final List<MultipartFile> imageFiles) {
        final List<StoreImageDto> storeImageDtos = new ArrayList<>();

        for (MultipartFile imageFile : imageFiles) {
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

            imageFile.transferTo(new File(fullPath));

            return new StoreImageDto(originalImageFileName, storeImageFileName);
        } catch (final IOException ex) {
            throw new StoreImageFailureException("이미지 저장에 실패했습니다.", ex);
        }
    }

    private String findFullPath(final String storeImageFileName) {
        return imageStoreDir + storeImageFileName;
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
