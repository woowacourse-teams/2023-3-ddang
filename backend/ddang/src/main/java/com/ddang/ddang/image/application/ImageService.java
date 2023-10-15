package com.ddang.ddang.image.application;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.repository.AuctionImageRepository;
import com.ddang.ddang.image.domain.repository.ProfileImageRepository;
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

    private static final String FILE_PROTOCOL_PREFIX = "file:";
    @Value("${image.store.dir}")
    private String imageStoreDir;

    private final ProfileImageRepository profileImageRepository;
    private final AuctionImageRepository auctionImageRepository;

    public Resource readProfileImage(final Long id) throws MalformedURLException {
        final ProfileImage profileImage = profileImageRepository.findById(id)
                                                                .orElse(null);

        if (profileImage == null) {
            return null;
        }

        final String fullPath = findFullPath(profileImage.getImage().getStoreName());

        return new UrlResource(FILE_PROTOCOL_PREFIX + fullPath);
    }

    public Resource readAuctionImage(final Long id) throws MalformedURLException {
        final AuctionImage auctionImage = auctionImageRepository.findById(id)
                                                                .orElse(null);

        if (auctionImage == null) {
            return null;
        }

        final String fullPath = findFullPath(auctionImage.getImage().getStoreName());

        return new UrlResource(FILE_PROTOCOL_PREFIX + fullPath);
    }

    private String findFullPath(final String storeImageFileName) {
        return imageStoreDir + storeImageFileName;
    }
}
