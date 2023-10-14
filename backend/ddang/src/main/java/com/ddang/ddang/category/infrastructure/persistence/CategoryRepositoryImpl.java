package com.ddang.ddang.category.infrastructure.persistence;

import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.domain.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;

    @Override
    public List<Category> findMainAllByMainCategoryIsNull() {
        return jpaCategoryRepository.findMainAllByMainCategoryIsNull();
    }

    @Override
    public List<Category> findSubAllByMainCategoryId(final Long mainCategoryId) {
        return jpaCategoryRepository.findSubAllByMainCategoryId(mainCategoryId);
    }

    @Override
    public Optional<Category> findSubCategoryById(final Long subCategoryId) {
        return jpaCategoryRepository.findSubCategoryById(subCategoryId);
    }
}
