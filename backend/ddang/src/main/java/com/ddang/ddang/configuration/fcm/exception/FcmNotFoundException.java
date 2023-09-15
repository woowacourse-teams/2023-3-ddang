package com.ddang.ddang.configuration.fcm.exception;

public class FcmNotFoundException extends RuntimeException {

    public FcmNotFoundException() {
        super("현재 Firebase App에 접근할 수 없습니다.");
    }
}
