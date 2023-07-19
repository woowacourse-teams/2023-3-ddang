package com.ddang.ddang.category.infrastructure.persistence;

import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import(QuerydslConfiguration.class)
class JpaCategoryRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaCategoryRepository categoryRepository;

    @Test
    void 모든_메인_카테고리를_조회한다() {
        // given
        final Category main1 = new Category("main1");
        final Category main2 = new Category("main2");
        final Category sub = new Category("sub");

        main1.addSubCategory(sub);

        categoryRepository.save(main1);
        categoryRepository.save(main2);

        em.flush();
        em.clear();

        // when
        final List<Category> actual = categoryRepository.findMainAllByMainCategoryIsNull();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 메인_카테고리에_해당하는_모든_서브_카테고리를_조회한다() {
        // given
        final Category main = new Category("main");
        final Category sub1 = new Category("sub1");
        final Category sub2 = new Category("sub2");

        main.addSubCategory(sub1);
        main.addSubCategory(sub2);

        categoryRepository.save(main);

        em.flush();
        em.clear();

        // when
        final List<Category> actual = categoryRepository.findSubAllByMainCategoryId(main.getId());

        // then
        assertThat(actual).hasSize(2);
    }
}
