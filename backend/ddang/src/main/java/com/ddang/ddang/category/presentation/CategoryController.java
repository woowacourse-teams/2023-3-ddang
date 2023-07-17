package com.ddang.ddang.category.presentation;

import com.ddang.ddang.category.application.CategoryService;
import com.ddang.ddang.category.application.dto.ReadCategoryDto;
import com.ddang.ddang.category.presentation.dto.ReadCategoriesResponse;
import com.ddang.ddang.category.presentation.dto.ReadCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ReadCategoriesResponse> readAllMain() {
        final List<ReadCategoryDto> readCategoryDtos = categoryService.readAllMain();
        final List<ReadCategoryResponse> readCategoryResponses = readCategoryDtos.stream()
                                                                                 .map(ReadCategoryResponse::from)
                                                                                 .toList();
        final ReadCategoriesResponse response = new ReadCategoriesResponse(readCategoryResponses);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{mainId}")
    public ResponseEntity<ReadCategoriesResponse> readAllSub(@PathVariable final Long mainId) {
        final List<ReadCategoryDto> readCategoryDtos = categoryService.readAllSubByMainId(mainId);
        final List<ReadCategoryResponse> readCategoryResponses = readCategoryDtos.stream()
                                                                                 .map(ReadCategoryResponse::from)
                                                                                 .toList();
        final ReadCategoriesResponse response = new ReadCategoriesResponse(readCategoryResponses);

        return ResponseEntity.ok(response);
    }
}
