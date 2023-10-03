package com.ddang.ddang.device.presentation.fixture;

import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.device.presentation.dto.request.UpdateDeviceTokenRequest;

@SuppressWarnings("NonAsciiCharacters")
public class DeviceTokenControllerFixture extends CommonControllerSliceTest {

    protected String 액세스_토큰_값 = "Bearer accessToken";
    protected String 유효하지_않은_액세스_토큰_값 = "Bearer invalidAccessToken";
    protected PrivateClaims 유효한_사용자_ID_클레임 = new PrivateClaims(1L);
    protected PrivateClaims 유효하지_않은_사용자_ID_클레임 = new PrivateClaims(-999L);
    protected UpdateDeviceTokenRequest 디바이스_토큰_갱신_요청 = new UpdateDeviceTokenRequest("newDeviceToken");
}
