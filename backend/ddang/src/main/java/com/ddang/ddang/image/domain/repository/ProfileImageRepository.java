package com.ddang.ddang.image.domain.repository;

import com.ddang.ddang.image.domain.ProfileImage;

import java.util.Optional;

public interface ProfileImageRepository {

    Optional<ProfileImage> findByStoreName(final String storeName);
}
