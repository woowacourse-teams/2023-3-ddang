package com.ddang.ddang.region.infrastructure.persistence;

import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.region.domain.Region;
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
class JpaRegionRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaRegionRepository regionRepository;

    @Test
    void 모든_첫번째_지역을_조회한다() {
        // given
        final Region first1 = new Region("first1");
        final Region first2 = new Region("first2");
        final Region second = new Region("second");

        first1.addSecondRegion(second);

        regionRepository.save(first1);
        regionRepository.save(first2);

        em.flush();
        em.clear();

        // when
        final List<Region> actual = regionRepository.findFirstAll();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 첫번째_지역에_해당하는_모든_두번째_지역을_조회한다() {
        // given
        final Region first = new Region("first");
        final Region second1 = new Region("second1");
        final Region second2 = new Region("second2");

        first.addSecondRegion(second1);
        first.addSecondRegion(second2);

        regionRepository.save(first);

        em.flush();
        em.clear();

        // when
        final List<Region> actual = regionRepository.findSecondAllByFirstRegionId(first.getId());

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 두번째_지역에_해당하는_모든_세번째_지역을_조회한다() {
        // given
        final Region first = new Region("first");
        final Region second = new Region("second");
        final Region third1 = new Region("third1");
        final Region third2 = new Region("third2");

        first.addSecondRegion(second);
        second.addThirdRegion(third1);
        second.addThirdRegion(third2);

        regionRepository.save(first);

        em.flush();
        em.clear();

        // when
        final List<Region> actual = regionRepository.findThirdAllByFirstAndSecondRegionId(first.getId(), second.getId());

        // then
        assertThat(actual).hasSize(2);
    }
}
