package com.ddang.ddang.region.infrastructure.persistence;

import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.domain.repository.RegionRepository;
import com.ddang.ddang.region.infrastructure.persistence.fixture.RegionRepositoryImplFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RegionRepositoryImplTest extends RegionRepositoryImplFixture {

    RegionRepository regionRepository;

    @BeforeEach
    void setUp(@Autowired JpaRegionRepository jpaRegionRepository) {
        regionRepository = new RegionRepositoryImpl(jpaRegionRepository);
    }

    @Test
    void 지역을_저장한다() {
        // given
        final Region region = new Region("region1");

        // when
        final Region actual = regionRepository.save(region);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 지역을_여러개_한번에_저장한다() {
        // given
        final Region region1 = new Region("region1");
        final Region region2 = new Region("region2");

        // when
        final List<Region> actual = regionRepository.saveAll(List.of(region1, region2));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(1).getId()).isPositive();
        });
    }

    @Test
    void 모든_첫번째_지역을_조회한다() {
        // when
        final List<Region> actual = regionRepository.findFirstAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(서울특별시);
            softAssertions.assertThat(actual.get(1)).isEqualTo(경기도);
        });
    }

    @Test
    void 첫번째_지역에_해당하는_모든_두번째_지역을_조회한다() {
        // when
        final List<Region> actual = regionRepository.findSecondAllByFirstRegionId(서울특별시.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(서울특별시_강남구);
            softAssertions.assertThat(actual.get(1)).isEqualTo(서울특별시_송파구);
        });
    }

    @Test
    void 두번째_지역에_해당하는_모든_세번째_지역을_조회한다() {
        // when
        final List<Region> actual =
                regionRepository.findThirdAllByFirstAndSecondRegionId(서울특별시.getId(), 서울특별시_강남구.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(서울특별시_강남구_삼성동);
            softAssertions.assertThat(actual.get(1)).isEqualTo(서울특별시_강남구_대치동);
        });
    }

    @Test
    void 세번째_지역을_조회한다() {
        // when
        final Optional<Region> actual = regionRepository.findThirdRegionById(서울특별시_강남구_삼성동.getId());

        // then
        assertThat(actual).contains(서울특별시_강남구_삼성동);
    }

    @Test
    void 세번째_지역이_아닌_아이디를_전달하면_빈_Optional을_반환한다() {
        // when
        final Optional<Region> actual = regionRepository.findThirdRegionById(서울특별시.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 세번째_지역에_해당하는_모든_id를_전달하면_그에_맞는_thirdRegions를_반환한다() {
        // when
        final List<Long> thirdRegionIds = List.of(서울특별시_강남구_삼성동.getId(), 서울특별시_강남구_대치동.getId());
        final List<Region> actual = regionRepository.findAllThirdRegionByIds(thirdRegionIds);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(서울특별시_강남구_삼성동);
            softAssertions.assertThat(actual.get(1)).isEqualTo(서울특별시_강남구_대치동);
        });
    }

    @Test
    void 세번째_지역이_아닌_아이디를_전달하면_빈_List를_반환한다() {
        // when
        final List<Region> actual = regionRepository.findAllThirdRegionByIds(List.of(서울특별시.getId()));

        // then
        assertThat(actual).isEmpty();
    }
}
