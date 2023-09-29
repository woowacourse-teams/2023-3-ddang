package com.ddang.ddang.region.application.fixture;

import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class RegionServiceFixture {

    @Autowired
    private JpaRegionRepository regionRepository;

    protected Region 서울특별시;
    protected Region 두번째_지역이_없는_첫번째_지역;
    protected Region 서울특별시_강남구;
    protected Region 세번째_지역이_없는_두번째_지역;
    protected Region 서울특별시_강남구_삼성동;
    protected Region 서울특별시_강남구_대치동;

    @BeforeEach
    void setUp() {
        서울특별시 = new Region("서울특별시");
        두번째_지역이_없는_첫번째_지역 = new Region("두번째 지역이 없는 첫번째 지역");
        서울특별시_강남구 = new Region("서울특별시 강남구");
        세번째_지역이_없는_두번째_지역 = new Region("세번째 지역이 없는 두번째 지역");
        서울특별시_강남구_삼성동 = new Region("서울특별시 강남구 삼성동");
        서울특별시_강남구_대치동 = new Region("서울특별시 강남구 대치동");

        서울특별시.addSecondRegion(서울특별시_강남구);
        서울특별시.addSecondRegion(세번째_지역이_없는_두번째_지역);

        서울특별시_강남구.addThirdRegion(서울특별시_강남구_삼성동);
        서울특별시_강남구.addThirdRegion(서울특별시_강남구_대치동);

        regionRepository.save(서울특별시);
        regionRepository.save(두번째_지역이_없는_첫번째_지역);
    }
}
