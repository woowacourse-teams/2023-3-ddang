package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.repository.ProfileImageRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileImageRepositoryImpl implements ProfileImageRepository {

    private final JpaProfileImageRepository jpaProfileImageRepository;

    @Override
    public Optional<ProfileImage> findByStoreName(final String storeName) {
        return jpaProfileImageRepository.findByStoreName(storeName);
    }
}
