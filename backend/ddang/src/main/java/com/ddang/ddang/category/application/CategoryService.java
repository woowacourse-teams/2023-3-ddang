package com.ddang.ddang.category.application;

import com.ddang.ddang.category.application.dto.ReadCategoryDto;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final JpaCategoryRepository categoryRepository;

    public List<ReadCategoryDto> readAllMain() {
        final List<Category> mainCategories = categoryRepository.findMainAllByMainCategoryIsNull();

        if (mainCategories.isEmpty()) {
            throw new CategoryNotFoundException("등록된 메인 카테고리가 없습니다.");
        }

        return mainCategories.stream()
                             .map(ReadCategoryDto::from)
                             .toList();
    }

    public List<ReadCategoryDto> readAllSubByMainId(final Long mainId) {
        final List<Category> subCategories = categoryRepository.findSubAllByMainCategoryId(mainId);

        if (subCategories.isEmpty()) {
            throw new CategoryNotFoundException("지정한 메인 카테고리에 해당 서브 카테고리가 없습니다.");
        }

        return subCategories.stream()
                            .map(ReadCategoryDto::from)
                            .toList();
    }
}
