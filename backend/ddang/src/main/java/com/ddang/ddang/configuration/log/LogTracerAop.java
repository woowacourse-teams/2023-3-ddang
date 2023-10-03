package com.ddang.ddang.configuration.log;

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
public class LogTracerAop {

    private static final String PROXY_CLASS_PREFIX = "Proxy";

    private final LogTracer logTracer;

    @Pointcut("@within(org.springframework.stereotype.Service)")
    private void serviceAnnotatedClass() {
    }

    @Pointcut("execution(* com.ddang.ddang..*Repository+.*(..))")
    private void repositoryClass() {
    }

    @Around("serviceAnnotatedClass() || repositoryClass()")
    public Object doLog(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (isNotRequestScope()) {
            return joinPoint.proceed();
        }

        final String className = findClassSimpleName(joinPoint);
        final String methodName = findMethodName(joinPoint);
        final TraceStatus status = logTracer.begin(className, methodName);

        try {
            final Object result = joinPoint.proceed();

            logTracer.end(status, className, methodName);
            return result;
        } catch (final Throwable ex) {
            logTracer.exception(status, className, methodName, ex);

            throw ex;
        }
    }

    private boolean isNotRequestScope() {
        return RequestContextHolder.getRequestAttributes() == null;
    }

    private String findClassSimpleName(final ProceedingJoinPoint joinPoint) {
        final Class<?> clazz = joinPoint.getTarget().getClass();
        final String className = clazz.getSimpleName();

        if (className.contains(PROXY_CLASS_PREFIX)) {
            return clazz.getInterfaces()[0].getSimpleName();
        }
        return className;
    }

    private String findMethodName(final ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }
}
