package com.ddang.ddang.region.domain.repository;

import com.ddang.ddang.region.domain.Region;
import java.util.List;

public interface RegionRepository {

    Region save(final Region region);

    List<Region> saveAll(final List<Region> regions);

    List<Region> findFirstAll();

    List<Region> findSecondAllByFirstRegionId(final Long firstRegionId);

    List<Region> findThirdAllByFirstAndSecondRegionId(final Long firstRegionId, final Long secondRegionId);

    List<Region> findAllThirdRegionByIds(final List<Long> thirdRegionIds);
}
