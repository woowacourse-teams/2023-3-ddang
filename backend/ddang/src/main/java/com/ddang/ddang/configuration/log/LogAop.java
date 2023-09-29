package com.ddang.ddang.configuration.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAop {

    private static final String PROXY_CLASS_PREFIX = "Proxy";

    private final LogTracer logTrace;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    private void restControllerAnnotatedClass() {
    }

    @Pointcut("@within(org.springframework.stereotype.Service)")
    private void serviceAnnotatedClass() {
    }

    @Pointcut("execution(* com.ddang.ddang..*Repository+.*(..))")
    private void repositoryClass() {
    }

    @Around("restControllerAnnotatedClass() || serviceAnnotatedClass() || repositoryClass()")
    public Object doLog(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (isNotRequestScope()) {
            return joinPoint.proceed();
        }

        final String methodName = getMethodName(joinPoint);
        final String className = getClassSimpleName(joinPoint);
        final TraceStatus status = logTrace.begin(className, methodName);

        try {
            final Object result = joinPoint.proceed();

            logTrace.end(status, className, methodName);
            return result;
        } catch (final Throwable ex) {
            logTrace.exception(status, className, methodName, ex);

            throw ex;
        }
    }

    private boolean isNotRequestScope() {
        return RequestContextHolder.getRequestAttributes() == null;
    }

    private String getClassSimpleName(ProceedingJoinPoint joinPoint) {
        final Class<?> clazz = joinPoint.getTarget().getClass();
        final String className = clazz.getSimpleName();

        if (className.contains(PROXY_CLASS_PREFIX)) {
            return clazz.getInterfaces()[0].getSimpleName();
        }
        return className;
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }
}
