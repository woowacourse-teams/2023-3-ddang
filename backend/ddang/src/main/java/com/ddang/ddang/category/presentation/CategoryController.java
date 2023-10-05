package com.ddang.ddang.category.presentation;

import com.ddang.ddang.category.application.CategoryService;
import com.ddang.ddang.category.application.dto.ReadCategoryDto;
import com.ddang.ddang.category.presentation.dto.response.ReadCategoryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Cacheable(cacheNames = "mainCategory")
    public ResponseEntity<List<ReadCategoryResponse>> readAllMain() {
        final List<ReadCategoryDto> readCategoryDtos = categoryService.readAllMain();
        final List<ReadCategoryResponse> readCategoryResponses = readCategoryDtos.stream()
                                                                                 .map(ReadCategoryResponse::from)
                                                                                 .toList();

        return ResponseEntity.ok(readCategoryResponses);
    }

    @GetMapping("/{mainId}")
    @Cacheable(cacheNames = "subCategory")
    public ResponseEntity<List<ReadCategoryResponse> > readAllSub(@PathVariable final Long mainId) {
        final List<ReadCategoryDto> readCategoryDtos = categoryService.readAllSubByMainId(mainId);
        final List<ReadCategoryResponse> readCategoryResponses = readCategoryDtos.stream()
                                                                                 .map(ReadCategoryResponse::from)
                                                                                 .toList();

        return ResponseEntity.ok(readCategoryResponses);
    }
}
