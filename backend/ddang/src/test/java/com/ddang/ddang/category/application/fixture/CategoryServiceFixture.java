package com.ddang.ddang.category.application.fixture;

import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class CategoryServiceFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    protected Category 가구_카테고리;
    protected Category 전자기기_카테고리;
    protected Category 가구_서브_의자_카테고리;
    protected Category 가구_서브_책상_카테고리;

    @BeforeEach
    void setUp() {
        가구_카테고리 = new Category("가구");
        전자기기_카테고리 = new Category("전자기기");
        가구_서브_의자_카테고리 = new Category("의자");
        가구_서브_책상_카테고리 = new Category("책상");

        가구_카테고리.addSubCategory(가구_서브_의자_카테고리);
        가구_카테고리.addSubCategory(가구_서브_책상_카테고리);

        categoryRepository.save(가구_카테고리);
        categoryRepository.save(전자기기_카테고리);
    }
}
