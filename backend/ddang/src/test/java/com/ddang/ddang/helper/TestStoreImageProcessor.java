package com.ddang.ddang.helper;

import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("test")
public class TestStoreImageProcessor implements StoreImageProcessor {

    @Override
    public StoreImageDto storeImageFile(final MultipartFile imageFile) {
        final String imageFilename = imageFile.getOriginalFilename();

        return new StoreImageDto(imageFilename, imageFilename);
    }

    @Override
    public List<StoreImageDto> storeImageFiles(final List<MultipartFile> imageFiles) {
        return imageFiles.stream()
                         .map(imageFile ->
                                 new StoreImageDto(imageFile.getOriginalFilename(), imageFile.getOriginalFilename()))
                         .toList();
    }
}
