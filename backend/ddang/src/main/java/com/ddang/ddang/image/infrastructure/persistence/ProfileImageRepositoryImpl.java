package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ProfileImageRepositoryImpl implements ProfileImageRepository {

    private final JpaProfileImageRepository jpaProfileImageRepository;

    @Override
    public Optional<ProfileImage> findById(final Long id) {
        return jpaProfileImageRepository.findById(id);
    }

    @Override
    public Optional<ProfileImage> findByStoreName(final String storeName) {
        return jpaProfileImageRepository.findByStoreName(storeName);
    }
}
