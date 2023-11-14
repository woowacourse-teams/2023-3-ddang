package com.ddang.ddang.category.infrastructure.persistence;

import com.ddang.ddang.category.infrastructure.exception.CategoryNotFoundException;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.domain.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
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
    public Category getSubCategoryByIdOrThrow(final Long subCategoryId) {
        return jpaCategoryRepository.findSubCategoryById(subCategoryId)
                                    .orElseThrow(() -> new CategoryNotFoundException(
                                            "지정한 하위 카테고리가 없거나 하위 카테고리가 아닙니다.")
                                    );
    }
}
