package com.ddang.ddang.region.infrastructure.persistence;

import com.ddang.ddang.region.domain.Region;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaRegionRepository extends JpaRepository<Region, Long> {

    @Query("select r from Region r where r.firstRegion.id is null and r.secondRegion.id is null")
    List<Region> findFirstAll();

    @Query("select r from Region r where r.firstRegion.id = :firstRegionId and r.secondRegion.id is null")
    List<Region> findSecondAllByFirstRegionId(final Long firstRegionId);

    @Query("select r from Region r where r.firstRegion.id = :firstRegionId and r.secondRegion.id = :secondRegionId")
    List<Region> findThirdAllByFirstAndSecondRegionId(final Long firstRegionId, final Long secondRegionId);

    @Query("""
             select r from Region r where r.id = :thirdRegionId and
             r.firstRegion.id is not null and
             r.secondRegion.id is not null
            """)
    Optional<Region> findThirdRegionById(final Long thirdRegionId);

    @Query("""
             select r from Region r where r.id in :thirdRegionIds and
             r.firstRegion.id is not null and
             r.secondRegion.id is not null
            """)
    List<Region> findAllThirdRegionByIds(final List<Long> thirdRegionIds);
}
