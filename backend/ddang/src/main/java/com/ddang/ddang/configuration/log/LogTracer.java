package com.ddang.ddang.configuration.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogTracer {

    private static final String START_PREFIX = "---> ";
    private static final String COMPLETE_PREFIX = "<--- ";
    private static final String EXCEPTION_PREFIX = "<-X- ";
    private static final String FIRST_DEPTH_SPACE = "|";
    private static final String OTHER_DEPTH_SPACE = "|    ";

    private final ThreadLocal<TraceDepth> traceIdHolder = new ThreadLocal<>();

    public TraceStatus begin(final String className, final String methodName) {
        syncTraceDepth();

        final TraceDepth traceId = traceIdHolder.get();
        final Long startTimeMs = System.currentTimeMillis();

        MDC.put("level", String.valueOf(traceId.getLevel()));
        MDC.put("resultTime", "NONE");

        log.info("{}", formattedClassAndMethod(addSpace(START_PREFIX, traceId.getLevel()), className, methodName));

        return new TraceStatus(traceId, startTimeMs, methodName);
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

    private String formattedClassAndMethod(final String prefix, final String className, final String methodName) {
        return prefix + className + "." + methodName + "()";
    }

    private String addSpace(final String prefix, final int level) {
        final StringBuilder spaceBuilder = new StringBuilder();

        for (int depth = 0; depth < level; depth++) {
            spaceBuilder.append(findNextSpace(prefix, depth, level));
        }

        return spaceBuilder.toString();
    }

    private String findNextSpace(final String prefix, final int depth, final int level) {
        if (depth == level - 1) {
            return FIRST_DEPTH_SPACE + prefix;
        }

        return OTHER_DEPTH_SPACE;
    }

    public void end(final TraceStatus status, final String className, final String methodName) {
        complete(status, className, methodName, null);
    }

    public void exception(TraceStatus status, final String className, final String methodName, final Throwable ex) {
        complete(status, className, methodName, ex);
    }

    private void complete(final TraceStatus status, final String className, final String methodName, final Throwable ex) {
        final Long stopTime = System.currentTimeMillis();
        final long resultTime = stopTime - status.getStartTime();
        final TraceDepth traceId = status.getTraceDepth();

        MDC.put("level", String.valueOf(traceId.getLevel()));
        MDC.put("resultTime", resultTime + "ms");

        if (ex == null) {
            log.info("{}", formattedClassAndMethod(addSpace(COMPLETE_PREFIX, traceId.getLevel()), className, methodName));
        } else {
            log.info("{}", formattedClassAndMethod(addSpace(EXCEPTION_PREFIX, traceId.getLevel()), className, methodName));
            MDC.put("exception", ex.getClass().getSimpleName());
            MDC.put("exceptionMessage", ex.getMessage());
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
