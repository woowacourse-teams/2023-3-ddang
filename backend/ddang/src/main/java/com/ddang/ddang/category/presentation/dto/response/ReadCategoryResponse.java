package com.ddang.ddang.category.presentation.dto.response;

import com.ddang.ddang.category.application.dto.response.ReadCategoryDto;

public record ReadCategoryResponse(Long id, String name) {

    public static ReadCategoryResponse from(final ReadCategoryDto dto) {
        return new ReadCategoryResponse(dto.id(), dto.name());
    }
}
