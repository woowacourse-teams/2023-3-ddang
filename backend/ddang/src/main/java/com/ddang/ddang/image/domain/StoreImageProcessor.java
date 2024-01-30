package com.ddang.ddang.image.domain;

import com.ddang.ddang.image.domain.dto.StoreImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreImageProcessor {

    List<String> WHITE_IMAGE_EXTENSION = List.of("jpg", "jpeg", "png");
    String EXTENSION_FILE_CHARACTER = ".";

    StoreImageDto storeImageFile(final MultipartFile imageFile);

    List<StoreImageDto> storeImageFiles(final List<MultipartFile> imageFiles);
}
