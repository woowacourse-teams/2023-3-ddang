package com.ddang.ddang.region.infrastructure.persistence;

import com.ddang.ddang.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaRegionRepository extends JpaRepository<Region, Long> {

    @Query("select r from Region r where r.firstRegion.id is null and r.secondRegion.id is null")
    List<Region> findFirstAll();

    @Query("select r from Region r where r.firstRegion.id = :firstRegionId and r.secondRegion.id is null")
    List<Region> findSecondAllByFirstRegionId(final Long firstRegionId);

    @Query("select r from Region r where r.firstRegion.id = :firstRegionId and r.secondRegion.id = :secondRegionId")
    List<Region> findThirdAllByFirstAndSecondRegionId(final Long firstRegionId, final Long secondRegionId);
}
