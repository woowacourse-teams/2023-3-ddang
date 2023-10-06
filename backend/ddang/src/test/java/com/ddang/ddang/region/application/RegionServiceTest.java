package com.ddang.ddang.region.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.ddang.ddang.region.application.dto.ReadRegionDto;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.domain.InitializationRegionProcessor;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RegionServiceTest {

    @MockBean
    InitializationRegionProcessor regionProcessor;

    @Autowired
    RegionService regionService;

    @Autowired
    JpaRegionRepository regionRepository;

    @Test
    void 대한민국_전국의_지역을_초기화한다() {
        // given
        final Region firstRegion = new Region("서울특별시");
        final Region secondRegion = new Region("송파구");
        final Region thirdRegion = new Region("가락1동");

        secondRegion.addThirdRegion(thirdRegion);
        firstRegion.addSecondRegion(secondRegion);
        given(regionProcessor.requestRegions()).willReturn(List.of(firstRegion));

        // when
        regionService.createRegions();

        // then
        final List<Region> actual = regionRepository.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);

            softAssertions.assertThat(actual.get(0)).isEqualTo(firstRegion);
            softAssertions.assertThat(actual.get(0).getSecondRegions()).hasSize(1);

            final Region actualSecondRegion = actual.get(0).getSecondRegions().get(0);
            softAssertions.assertThat(actualSecondRegion).isEqualTo(secondRegion);
            softAssertions.assertThat(actualSecondRegion.getThirdRegions()).hasSize(1);

            final Region actualThirdRegion = actualSecondRegion.getThirdRegions().get(0);
            softAssertions.assertThat(actualThirdRegion).isEqualTo(thirdRegion);
        });
    }

    @Test
    void 모든_첫번째_지역을_조회한다() {
        // given
        final Region first1 = new Region("first1");
        final Region first2 = new Region("first2");
        final Region second = new Region("second");

        first1.addSecondRegion(second);

        regionRepository.save(first1);
        regionRepository.save(first2);

        // when
        final List<ReadRegionDto> actual = regionService.readAllFirst();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 첫번째_지역이_없는_경우_지역_조회시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> regionService.readAllFirst())
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("등록된 지역이 없습니다.");
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

        // when
        final List<ReadRegionDto> actual = regionService.readAllSecondByFirstRegionId(first.getId());

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 지정한_첫번째_지역에_해당하는_두번째_지역이_없는_경우_두번째_지역_조회시_예외가_발생한다() {
        // given
        final Region first = new Region("first");

        regionRepository.save(first);

        // when & then
        assertThatThrownBy(() -> regionService.readAllSecondByFirstRegionId(first.getId()))
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("지정한 첫 번째 지역에 해당하는 두 번째 지역이 없습니다.");
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

        // when
        final List<ReadRegionDto> actual = regionService.readAllThirdByFirstAndSecondRegionId(first.getId(), second.getId());

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 지정한_첫번째와_두번째_지역에_해당하는_세번째_지역이_없는_경우_세번째_지역_조회시_예외가_발생한다() {
        // given
        final Region first = new Region("first");
        final Region second = new Region("second");

        first.addSecondRegion(second);

        regionRepository.save(first);

        // when & then
        assertThatThrownBy(() -> regionService.readAllThirdByFirstAndSecondRegionId(first.getId(), second.getId()))
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("지정한 첫 번째와 두 번째 지역에 해당하는 세 번째 지역이 없습니다.");
    }
}
