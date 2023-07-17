package com.ddang.ddang.category.presentation.dto;

import com.ddang.ddang.category.application.dto.ReadCategoryDto;

public record ReadCategoryResponse(Long id, String name) {

    public static ReadCategoryResponse from(final ReadCategoryDto dto) {
        return new ReadCategoryResponse(dto.id(), dto.name());
    }
}
