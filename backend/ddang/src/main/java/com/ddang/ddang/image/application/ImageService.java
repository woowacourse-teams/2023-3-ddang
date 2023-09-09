package com.ddang.ddang.image.application;

import com.ddang.ddang.image.domain.Image;
import com.ddang.ddang.image.application.exception.ImageNotFoundException;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.JpaImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    @Value("${image.store.dir}")
    private String imageStoreDir;

    private final JpaImageRepository imageRepository;
    private final JpaAuctionImageRepository auctionImageRepository;

    public Resource readImage(final Long id) throws MalformedURLException {
        final Image image = imageRepository.findById(id)
                                           .orElseThrow(() -> new ImageNotFoundException(
                                                   "지정한 이미지를 찾을 수 없습니다."
                                           ));
        final String fullPath = findFullPath(image.getStoreName());

        return new UrlResource("file:" + fullPath);
    }

    public Resource readAuctionImage(final Long id) throws MalformedURLException {
        final AuctionImage auctionImage = auctionImageRepository.findById(id)
                                                                .orElseThrow(() -> new ImageNotFoundException(
                                                                        "지정한 이미지를 찾을 수 없습니다."
                                                                ));
        final String fullPath = findFullPath(auctionImage.getStoreName());

        return new UrlResource("file:" + fullPath);
    }

    private String findFullPath(String storeImageFileName) {
        return imageStoreDir + storeImageFileName;
    }
}
