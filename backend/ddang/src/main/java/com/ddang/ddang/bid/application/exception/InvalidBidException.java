package com.ddang.ddang.bid.application.exception;

// TODO: 2023/07/30 [고민] 상위 excpetion 클래스 위치는 어디가 좋은가?
public class InvalidBidException extends IllegalArgumentException {

    public InvalidBidException(final String message) {
        super(message);
    }
}
