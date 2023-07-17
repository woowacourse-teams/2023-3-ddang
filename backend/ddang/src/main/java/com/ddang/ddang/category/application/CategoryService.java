package com.ddang.ddang.category.application;

import com.ddang.ddang.category.application.dto.ReadCategoryDto;
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
        return categoryRepository.findMainAllByMainCategoryIsNull()
                                 .stream()
                                 .map(ReadCategoryDto::from)
                                 .toList();
    }

    public List<ReadCategoryDto> readAllSubByMainId(Long mainId) {
        return categoryRepository.findSubAllByMainCategoryId(mainId)
                                 .stream()
                                 .map(ReadCategoryDto::from)
                                 .toList();
    }
}
