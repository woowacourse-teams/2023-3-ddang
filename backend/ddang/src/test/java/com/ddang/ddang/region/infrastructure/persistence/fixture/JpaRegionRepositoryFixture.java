package com.ddang.ddang.region.infrastructure.persistence.fixture;

import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class JpaRegionRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaRegionRepository regionRepository;

    protected Region 서울특별시;
    protected Region 경기도;
    protected Region 서울특별시_강남구;
    protected Region 서울특별시_송파구;
    protected Region 서울특별시_강남구_삼성동;
    protected Region 서울특별시_강남구_대치동;

    @BeforeEach
    void setUp() {
        서울특별시 = new Region("서울특별시");
        경기도 = new Region("경기도");
        서울특별시_강남구 = new Region("강남구");
        서울특별시_송파구 = new Region("송파구");
        서울특별시_강남구_삼성동 = new Region("삼성동");
        서울특별시_강남구_대치동 = new Region("대치동");

        서울특별시.addSecondRegion(서울특별시_강남구);
        서울특별시.addSecondRegion(서울특별시_송파구);

        서울특별시_강남구.addThirdRegion(서울특별시_강남구_삼성동);
        서울특별시_강남구.addThirdRegion(서울특별시_강남구_대치동);

        regionRepository.save(서울특별시);
        regionRepository.save(경기도);

        em.flush();
        em.clear();
    }
}
