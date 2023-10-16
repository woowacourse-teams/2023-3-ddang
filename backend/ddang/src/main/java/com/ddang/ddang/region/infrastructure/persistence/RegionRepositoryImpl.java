package com.ddang.ddang.region.infrastructure.persistence;

import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.domain.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RegionRepositoryImpl implements RegionRepository {

    private final JpaRegionRepository jpaRegionRepository;

    @Override
    public Region save(final Region region) {
        return jpaRegionRepository.save(region);
    }

    @Override
    public List<Region> saveAll(final List<Region> regions) {
        return jpaRegionRepository.saveAll(regions);
    }

    @Override
    public List<Region> findFirstAll() {
        return jpaRegionRepository.findFirstAll();
    }

    @Override
    public List<Region> findSecondAllByFirstRegionId(final Long firstRegionId) {
        return jpaRegionRepository.findSecondAllByFirstRegionId(firstRegionId);
    }

    @Override
    public List<Region> findThirdAllByFirstAndSecondRegionId(final Long firstRegionId, final Long secondRegionId) {
        return jpaRegionRepository.findThirdAllByFirstAndSecondRegionId(firstRegionId, secondRegionId);
    }

    @Override
    public Optional<Region> findThirdRegionById(final Long thirdRegionId) {
        return jpaRegionRepository.findThirdRegionById(thirdRegionId);
    }

    @Override
    public List<Region> findAllThirdRegionByIds(final List<Long> thirdRegionIds) {
        return jpaRegionRepository.findAllThirdRegionByIds(thirdRegionIds);
    }
}
