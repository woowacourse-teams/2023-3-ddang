package com.ddang.ddang.region.infrastructure.api.fixture;

import com.ddang.ddang.region.infrastructure.api.dto.ApiAccessTokenResponse;
import com.ddang.ddang.region.infrastructure.api.dto.ResultApiRegionResponse;
import com.ddang.ddang.region.infrastructure.api.dto.TotalApiRegionResponse;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class RestTemplateInitRegionProcessorFixture {

    private String 액세스_토큰 = "accessToken";
    private String 서울특별시_지역_코드 = "11";
    private String 강남구_지역_코드 = "11230";
    private String 개포1동_지역_코드 = "11230680";
    private String 단계별_지역_목록_조회_URI = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json";

    protected String 액세스_토큰_요청_URI_패턴 = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json.*";
    protected ApiAccessTokenResponse 유효한_액세스_토큰 = new ApiAccessTokenResponse(
            Collections.singletonMap("accessToken", 액세스_토큰)
    );
    protected ApiAccessTokenResponse 유효하지_않은_액세스_토큰 = new ApiAccessTokenResponse(null);
    protected String 단계별_지역_목록_조회_URI_패턴 = 단계별_지역_목록_조회_URI + ".*";
    protected String 첫번쪠_지역_목록_조회_URI = 단계별_지역_목록_조회_URI + "?accessToken=" + 액세스_토큰;
    protected String 서울특별시_하위_지역_목록_조회_URI =
            단계별_지역_목록_조회_URI + "?accessToken=" + 액세스_토큰 + "&cd=" + 서울특별시_지역_코드;
    protected String 서울특별시_강남구_하위_지역_목록_조회_URI =
            단계별_지역_목록_조회_URI + "?accessToken=" + 액세스_토큰 + "&cd=" + 강남구_지역_코드;
    protected ResultApiRegionResponse 서울특별시 = new ResultApiRegionResponse("서울특별시", 서울특별시_지역_코드);
    protected ResultApiRegionResponse 강남구 = new ResultApiRegionResponse("강남구", 강남구_지역_코드);
    protected ResultApiRegionResponse 개포1동 = new ResultApiRegionResponse("개포1동", 개포1동_지역_코드);
    protected TotalApiRegionResponse 첫번째_지역_목록_조회_응답 = new TotalApiRegionResponse(List.of(서울특별시));
    protected TotalApiRegionResponse 서울특별시_하위_지역_목록_조회_응답 = new TotalApiRegionResponse(List.of(강남구));
    protected TotalApiRegionResponse 서울특별시_강남구_하위_지역_목록_조회_응답 = new TotalApiRegionResponse(List.of(개포1동));
}
