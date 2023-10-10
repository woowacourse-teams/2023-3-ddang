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
        final Long startTime = System.currentTimeMillis();
        final String methodSignature = formattedMethodSignature(className, methodName);

        MDC.put("level", String.valueOf(traceId.getLevel()));
        MDC.put("resultTime", "NONE");
        MDC.put("class.method", methodSignature);

        log.info("{}", formattedDepth(addSpace(START_PREFIX, traceId.getLevel()), methodSignature));

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

    private String formattedDepth(final String prefix, final String methodSignature) {
        return prefix + methodSignature;
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

    private void complete(
            final TraceStatus status,
            final String className,
            final String methodName,
            final Throwable ex
    ) {
        final Long stopTime = System.currentTimeMillis();
        final long resultTime = stopTime - status.getStartTime();
        final TraceDepth traceId = status.getTraceDepth();
        final String methodSignature = formattedMethodSignature(className, methodName);

        MDC.put("level", String.valueOf(traceId.getLevel()));
        MDC.put("resultTime", resultTime + "ms");
        MDC.put("class.method", methodSignature);

        if (ex == null) {
            log.info("{}", formattedDepth(addSpace(COMPLETE_PREFIX, traceId.getLevel()), methodSignature));
        } else {
            log.info("{} : {}", formattedDepth(
                            addSpace(EXCEPTION_PREFIX, traceId.getLevel()), methodSignature),
                    ex.getClass().getSimpleName()
            );
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

    public void exception(
            final TraceStatus status,
            final String className,
            final String methodName,
            final Throwable ex
    ) {
        complete(status, className, methodName, ex);
    }
}
