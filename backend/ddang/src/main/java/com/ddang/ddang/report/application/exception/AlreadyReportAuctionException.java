package com.ddang.ddang.report.application.exception;

public class AlreadyReportAuctionException extends IllegalArgumentException {

    public AlreadyReportAuctionException(final String message) {
        super(message);
    }
}
