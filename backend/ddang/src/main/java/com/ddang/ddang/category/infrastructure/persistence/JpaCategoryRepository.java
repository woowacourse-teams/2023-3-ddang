package com.ddang.ddang.category.infrastructure.persistence;

import com.ddang.ddang.category.domain.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findMainAllByMainCategoryIsNull();

    List<Category> findSubAllByMainCategoryId(final Long mainCategoryId);

    @Query("select c from Category c where c.id = :subCategoryId and c.mainCategory.id is not null")
    Optional<Category> findSubCategoryById(final Long subCategoryId);
}
