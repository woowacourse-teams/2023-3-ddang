package com.ddang.ddang.region.application;

import com.ddang.ddang.region.application.dto.response.ReadRegionDto;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.domain.InitializationRegionProcessor;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.domain.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final InitializationRegionProcessor regionProcessor;

    @Transactional
    public void createRegions() {
        final List<Region> regions = regionProcessor.requestRegions();

        regionRepository.saveAll(regions);
    }

    public List<ReadRegionDto> readAllFirst() {
        final List<Region> firstRegions = regionRepository.findFirstAll();

        if (firstRegions.isEmpty()) {
            throw new RegionNotFoundException("등록된 지역이 없습니다.");
        }

        return firstRegions.stream()
                           .map(ReadRegionDto::from)
                           .toList();
    }

    public List<ReadRegionDto> readAllSecondByFirstRegionId(final Long firstRegionId) {
        final List<Region> secondRegions = regionRepository.findSecondAllByFirstRegionId(firstRegionId);

        if (secondRegions.isEmpty()) {
            throw new RegionNotFoundException("지정한 첫 번째 지역에 해당하는 두 번째 지역이 없습니다.");
        }

        return secondRegions.stream()
                            .map(ReadRegionDto::from)
                            .toList();
    }

    public List<ReadRegionDto> readAllThirdByFirstAndSecondRegionId(
            final Long firstRegionId,
            final Long secondRegionId
    ) {
        final List<Region> thirdRegions = regionRepository.findThirdAllByFirstAndSecondRegionId(
                firstRegionId,
                secondRegionId
        );

        if (thirdRegions.isEmpty()) {
            throw new RegionNotFoundException("지정한 첫 번째와 두 번째 지역에 해당하는 세 번째 지역이 없습니다.");
        }

        return thirdRegions.stream()
                           .map(ReadRegionDto::from)
                           .toList();
    }
}
