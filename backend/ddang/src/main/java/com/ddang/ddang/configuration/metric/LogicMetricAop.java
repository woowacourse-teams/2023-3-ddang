package com.ddang.ddang.configuration.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
@RequiredArgsConstructor
public class LogicMetricAop {

    private final MeterRegistry registry;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    private void restControllerAnnotatedClass() {
    }

    @Around("restControllerAnnotatedClass()")
    public Object doLog(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (isRequestScope()) {
            final String className = findClassSimpleName(joinPoint);
            final String methodName = findMethodName(joinPoint);

            Counter.builder("logic")
                   .tag("class", className)
                   .tag("method", methodName)
                   .tag("class.method", formattedTag(className, methodName))
                   .description("비즈니스 로직 메트릭")
                   .register(registry)
                   .increment();
        }

        return joinPoint.proceed();
    }

    private boolean isRequestScope() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

    private String findClassSimpleName(final ProceedingJoinPoint joinPoint) {
        final Class<?> clazz = joinPoint.getTarget().getClass();

        return clazz.getSimpleName();
    }

    private String findMethodName(final ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

    private String formattedTag(final String className, final String methodName) {
        return className + "." + methodName;
    }
}
