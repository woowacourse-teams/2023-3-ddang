package com.ddang.ddang.region.application;

import com.ddang.ddang.region.application.dto.ReadRegionDto;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.application.fixture.RegionServiceFixture;
import com.ddang.ddang.region.domain.InitializationRegionProcessor;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RegionServiceTest extends RegionServiceFixture {

    @MockBean
    InitializationRegionProcessor regionProcessor;

    @Autowired
    RegionService regionService;

    @Autowired
    JpaRegionRepository regionRepository;

    @Test
    void 대한민국_전국의_지역을_초기화한다() {
        // given
        given(regionProcessor.requestRegions()).willReturn(List.of(서울특별시, 두번째_지역이_없는_첫번째_지역));

        // when
        regionService.createRegions();

        // then
        final List<Region> actual = regionRepository.findAll();

        // TODO: 9/30/23 여기 너무 극혐인데 어떻게 하면 깔끔하게 할 수 있을까요.. 
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(6);

            final Region actualFirstRegion1 = actual.get(0);
            softAssertions.assertThat(actualFirstRegion1).isEqualTo(서울특별시);
            final Region actualFirstRegion2 = actual.get(5);
            softAssertions.assertThat(actualFirstRegion2).isEqualTo(두번째_지역이_없는_첫번째_지역);

            softAssertions.assertThat(actualFirstRegion1.getSecondRegions()).hasSize(2);
            final Region actualSecondRegion1OfFirstRegion1 = actualFirstRegion1.getSecondRegions().get(0);
            softAssertions.assertThat(actualSecondRegion1OfFirstRegion1).isEqualTo(서울특별시_강남구);
            final Region actualSecondRegion1OfFirstRegion2 = actualFirstRegion1.getSecondRegions().get(1);
            softAssertions.assertThat(actualSecondRegion1OfFirstRegion2).isEqualTo(세번째_지역이_없는_두번째_지역);

            softAssertions.assertThat(actualSecondRegion1OfFirstRegion1.getThirdRegions()).hasSize(2);
            final Region actualThirdRegion1OfSecondRegion1 = actualSecondRegion1OfFirstRegion1.getThirdRegions().get(0);
            softAssertions.assertThat(actualThirdRegion1OfSecondRegion1).isEqualTo(서울특별시_강남구_삼성동);
            final Region actualThirdRegion1OfSecondRegion2 = actualSecondRegion1OfFirstRegion1.getThirdRegions().get(1);
            softAssertions.assertThat(actualThirdRegion1OfSecondRegion2).isEqualTo(서울특별시_강남구_대치동);
        });
    }

    @Test
    void 모든_첫번째_지역을_조회한다() {
        // when
        final List<ReadRegionDto> actual = regionService.readAllFirst();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(서울특별시.getId());
            softAssertions.assertThat(actual.get(1).id()).isEqualTo(두번째_지역이_없는_첫번째_지역.getId());
        });
    }

    @Test
    void 첫번째_지역에_해당하는_모든_두번째_지역을_조회한다() {
        // when
        final List<ReadRegionDto> actual = regionService.readAllSecondByFirstRegionId(서울특별시.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(서울특별시_강남구.getId());
            softAssertions.assertThat(actual.get(1).id()).isEqualTo(세번째_지역이_없는_두번째_지역.getId());
        });
    }

    @Test
    void 지정한_첫번째_지역에_해당하는_두번째_지역이_없는_경우_두번째_지역_조회시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> regionService.readAllSecondByFirstRegionId(두번째_지역이_없는_첫번째_지역.getId()))
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("지정한 첫 번째 지역에 해당하는 두 번째 지역이 없습니다.");
    }

    @Test
    void 두번째_지역에_해당하는_모든_세번째_지역을_조회한다() {
        // when
        final List<ReadRegionDto> actual = 
                regionService.readAllThirdByFirstAndSecondRegionId(서울특별시.getId(), 서울특별시_강남구.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(서울특별시_강남구_삼성동.getId());
            softAssertions.assertThat(actual.get(1).id()).isEqualTo(서울특별시_강남구_대치동.getId());
        });
    }

    @Test
    void 지정한_첫번째와_두번째_지역에_해당하는_세번째_지역이_없는_경우_세번째_지역_조회시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> regionService.readAllThirdByFirstAndSecondRegionId(서울특별시.getId(), 세번째_지역이_없는_두번째_지역.getId()))
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("지정한 첫 번째와 두 번째 지역에 해당하는 세 번째 지역이 없습니다.");
    }
}
