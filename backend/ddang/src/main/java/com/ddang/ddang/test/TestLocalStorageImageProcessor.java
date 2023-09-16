package com.ddang.ddang.test;

import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.image.infrastructure.local.exception.EmptyImageException;
import com.ddang.ddang.image.infrastructure.local.exception.StoreImageFailureException;
import com.ddang.ddang.image.infrastructure.local.exception.UnsupportedImageFileExtensionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Primary
public class TestLocalStorageImageProcessor implements StoreImageProcessor {

    private static final List<String> WHITE_IMAGE_EXTENSION = List.of("jpg", "jpeg", "png");
    private static final String EXTENSION_FILE_CHARACTER = ".";

//    @Value("${image.store.dir}")
    private String imageStoreDir = "/Users/jamie/test-3ddang/";

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

    public StoreImageDto storeImageFile(MultipartFile imageFile) {
        final String originalImageFileName = imageFile.getOriginalFilename();
        final String storeImageFileName = createStoreImageFileName(originalImageFileName);
        final String fullPath = findFullPath(storeImageFileName);

        System.out.println(fullPath);

//            imageFile.transferTo(new File(fullPath));

        return new StoreImageDto(storeImageFileName, storeImageFileName);

    }

    private String findFullPath(String storeImageFileName) {
        return imageStoreDir + storeImageFileName;
    }

    private String createStoreImageFileName(String originalFilename) {
        final String extension = extractExtension(originalFilename);

        validateImageFileExtension(extension);

        final String uuid = UUID.randomUUID().toString();

        return uuid + EXTENSION_FILE_CHARACTER + extension;
    }

    private String extractExtension(String originalFilename) {
        int position = originalFilename.lastIndexOf(EXTENSION_FILE_CHARACTER);

        return originalFilename.substring(position + 1);
    }

    private void validateImageFileExtension(final String extension) {
        if (!WHITE_IMAGE_EXTENSION.contains(extension)) {
            throw new UnsupportedImageFileExtensionException("지원하지 않는 확장자입니다. : " + extension);
        }
    }
}
