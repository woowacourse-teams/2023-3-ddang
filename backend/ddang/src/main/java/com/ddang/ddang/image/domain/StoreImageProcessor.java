package com.ddang.ddang.image.domain;

import com.ddang.ddang.image.domain.dto.StoreImageDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StoreImageProcessor {

    List<StoreImageDto> storeImageFiles(List<MultipartFile> imageFiles);
}
