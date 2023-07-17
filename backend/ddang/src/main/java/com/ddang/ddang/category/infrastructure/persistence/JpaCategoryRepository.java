package com.ddang.ddang.category.infrastructure.persistence;

import com.ddang.ddang.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findMainAllByMainCategoryIsNull();

    List<Category> findSubAllByMainCategoryId(Long mainCategoryId);
}
