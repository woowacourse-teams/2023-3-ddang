package com.ddang.ddang.image.domain;

import com.ddang.ddang.image.domain.dto.StoreImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreImageProcessor {

    StoreImageDto storeImageFile(MultipartFile imageFile);

    List<StoreImageDto> storeImageFiles(List<MultipartFile> imageFiles);
}
