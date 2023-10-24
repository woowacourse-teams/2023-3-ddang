package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    @Query("SELECT i FROM ProfileImage i WHERE i.image.storeName = :storeName")
    Optional<ProfileImage> findByStoreName(final String storeName);
}
