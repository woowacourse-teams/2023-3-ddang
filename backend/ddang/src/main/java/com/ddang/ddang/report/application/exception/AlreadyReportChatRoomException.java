package com.ddang.ddang.report.application.exception;

public class AlreadyReportChatRoomException extends IllegalArgumentException {

    public AlreadyReportChatRoomException(final String message) {
        super(message);
    }
}
