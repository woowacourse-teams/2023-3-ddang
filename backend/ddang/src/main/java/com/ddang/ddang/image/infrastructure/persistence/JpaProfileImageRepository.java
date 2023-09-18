package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
