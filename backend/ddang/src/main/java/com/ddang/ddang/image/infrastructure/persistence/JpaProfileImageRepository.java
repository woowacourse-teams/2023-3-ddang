package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    @Query("""
        SELECT COUNT(profile_image) > 0
        FROM ProfileImage profile_image
        WHERE profile_image.image.storeName = :storeName
    """)
    boolean existsByStoreName(final String storeName);
}
