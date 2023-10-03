package com.ddang.ddang.configuration.log;

public class TraceDepth {

    private final int level;

    public TraceDepth() {
        this.level = 0;
    }

    private TraceDepth(final int level) {
        this.level = level;
    }

    public TraceDepth createNextId() {
        return new TraceDepth(level + 1);
    }

    public TraceDepth createPreviousId() {
        return new TraceDepth(level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

    public int getLevel() {
        return level;
    }
}
