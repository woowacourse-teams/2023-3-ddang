package com.ddang.ddang.category.application.dto;

import com.ddang.ddang.category.domain.Category;

public record ReadCategoryDto(Long id, String name) {

    public static ReadCategoryDto from(final Category category) {
        return new ReadCategoryDto(category.getId(), category.getName());
    }
}
