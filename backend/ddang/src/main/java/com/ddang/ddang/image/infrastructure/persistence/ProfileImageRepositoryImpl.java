package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProfileImageRepositoryImpl implements ProfileImageRepository {

    private final JpaProfileImageRepository jpaProfileImageRepository;

    @Override
    public boolean existsByStoreName(final String storeName) {
        return jpaProfileImageRepository.existsByStoreName(storeName);
    }
}
