package com.ddang.ddang.category.infrastructure.persistence.fixture;

import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfiguration.class)
public class JpaCategoryRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    protected Category 가구_카테고리;
    protected Category 전자기기_카테고리;
    protected Category 가구_하위_의자_카테고리;
    protected Category 가구_하위_책상_카테고리;

    @BeforeEach
    void setUp() {
        가구_카테고리 = new Category("가구");
        전자기기_카테고리 = new Category("전자기기");
        가구_하위_의자_카테고리 = new Category("의자");
        가구_하위_책상_카테고리 = new Category("책상");

        가구_카테고리.addSubCategory(가구_하위_의자_카테고리);
        가구_카테고리.addSubCategory(가구_하위_책상_카테고리);

        categoryRepository.save(가구_카테고리);
        categoryRepository.save(전자기기_카테고리);

        em.flush();
        em.clear();
    }
}
