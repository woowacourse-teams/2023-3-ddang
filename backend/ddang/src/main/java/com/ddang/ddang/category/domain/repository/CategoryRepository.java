package com.ddang.ddang.category.domain.repository;

import com.ddang.ddang.category.domain.Category;
import java.util.List;

public interface CategoryRepository {

    List<Category> findMainAllByMainCategoryIsNull();

    List<Category> findSubAllByMainCategoryId(final Long mainCategoryId);

    Category getSubCategoryByIdOrThrow(final Long subCategoryId);
}
