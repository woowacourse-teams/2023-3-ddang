package com.ddang.ddang.configuration.log;

public class TraceStatus {

    private final TraceDepth traceDepth;
    private final Long startTime;
    private final String message;

    public TraceStatus(final TraceDepth traceDepth, final Long startTime, final String message) {
        this.traceDepth = traceDepth;
        this.startTime = startTime;
        this.message = message;
    }

    public TraceDepth getTraceDepth() {
        return traceDepth;
    }

    public Long getStartTime() {
        return startTime;
    }

    public String getMessage() {
        return message;
    }
}
