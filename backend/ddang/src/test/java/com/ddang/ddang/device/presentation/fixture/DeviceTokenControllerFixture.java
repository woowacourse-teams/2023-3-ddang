package com.ddang.ddang.device.presentation.fixture;

import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.device.presentation.dto.request.UpdateDeviceTokenRequest;

@SuppressWarnings("NonAsciiCharacters")
public class DeviceTokenControllerFixture extends CommonControllerSliceTest {

    protected PrivateClaims 유효한_비공개_클레임 = new PrivateClaims(1L);
    protected PrivateClaims 유효하지_않은_비공개_클레임 = new PrivateClaims(-999L);
    protected UpdateDeviceTokenRequest 디바이스_토큰_갱신_요청 = new UpdateDeviceTokenRequest("newDeviceToken");
}
