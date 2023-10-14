package com.ddang.ddang.category.domain.repository;

import com.ddang.ddang.category.domain.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<Category> findMainAllByMainCategoryIsNull();

    List<Category> findSubAllByMainCategoryId(final Long mainCategoryId);

    Optional<Category> findSubCategoryById(final Long subCategoryId);
}
