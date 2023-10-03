package com.ddang.ddang.configuration.log;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestResponseLogProcessor {

    private final ThreadLocal<TraceDepth> traceIdHolder = new ThreadLocal<>();

    public TraceStatus begin(final String className, final String methodName, final Object[] requestArgs) {
        syncTraceDepth();

        final TraceDepth traceId = traceIdHolder.get();
        final Long startTime = System.currentTimeMillis();

        MDC.put("level", String.valueOf(traceId.getLevel()));
        MDC.put("resultTime", "NONE");
        MDC.put("class.method", formattedMethodSignature(className, methodName));

        log.info("{}", Markers.appendArray("arguments", requestArgs));

        return new TraceStatus(traceId, startTime, methodName);
    }

    private void syncTraceDepth() {
        final TraceDepth traceId = traceIdHolder.get();

        traceIdHolder.set(findNextTraceDepth(traceId));
    }

    private TraceDepth findNextTraceDepth(final TraceDepth traceId) {
        if (traceId == null) {
            return new TraceDepth();
        }

        return traceId.createNextId();
    }

    private String formattedMethodSignature(final String className, final String methodName) {
        return className + "." + methodName + "()";
    }

    public void end(final TraceStatus status, final String className, final String methodName, final Object result) {
        complete(status, className, methodName, result, null);
    }

    public void exception(
            final TraceStatus status,
            final String className,
            final String methodName,
            final Throwable ex
    ) {
        complete(status, className, methodName, null, ex);
    }

    private void complete(
            final TraceStatus status,
            final String className,
            final String methodName,
            final Object target,
            final Throwable ex
    ) {
        final Long stopTime = System.currentTimeMillis();
        final long resultTime = stopTime - status.getStartTime();
        final TraceDepth traceId = status.getTraceDepth();

        MDC.put("level", String.valueOf(traceId.getLevel()));
        MDC.put("resultTime", resultTime + "ms");
        MDC.put("class.method", formattedMethodSignature(className, methodName));

        if (target != null && ex == null) {
            log.info("{}", target);
        } else {
            log.info("{} : {}", ex.getClass().getSimpleName(), ex.getMessage());
        }

        releaseTraceDepth();
    }

    private void releaseTraceDepth() {
        final TraceDepth traceId = traceIdHolder.get();

        if (traceId.isFirstLevel()) {
            traceIdHolder.remove();
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }
}
