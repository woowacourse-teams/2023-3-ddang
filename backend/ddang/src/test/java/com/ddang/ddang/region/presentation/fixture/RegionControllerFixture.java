package com.ddang.ddang.region.presentation.fixture;

import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.region.application.dto.ReadRegionDto;

@SuppressWarnings("NonAsciiCharacters")
public class RegionControllerFixture extends CommonControllerSliceTest {

    protected ReadRegionDto 서울특별시 = new ReadRegionDto(1L, "서울특별시");
    protected ReadRegionDto 부산광역시 = new ReadRegionDto(2L, "부산광역시");
    protected ReadRegionDto 두번째_지역이_없는_첫번째_지역 = new ReadRegionDto(3L, "두번째 지역이 없는 첫번째 지역");
    protected ReadRegionDto 서울특별시_하위_강남구 = new ReadRegionDto(4L, "강남구");
    protected ReadRegionDto 서울특별시_하위_강동구 = new ReadRegionDto(5L, "강동구");
    protected ReadRegionDto 세번째_지역이_없는_두번째_지역 = new ReadRegionDto(6L, "세번째 지역이 없는 두번째 지역");
    protected ReadRegionDto 서울특별시_하위_강남구_하위_개포1동 = new ReadRegionDto(7L, "개포1동");
    protected ReadRegionDto 서울특별시_하위_강남구_하위_개포2동 = new ReadRegionDto(8L, "개포2동");
}
