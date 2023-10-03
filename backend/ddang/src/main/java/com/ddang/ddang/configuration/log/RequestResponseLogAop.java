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
public class RequestResponseLogAop {

    private final RequestResponseLogProcessor logProcessor;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    private void restControllerAnnotatedClass() {
    }

    @Around("restControllerAnnotatedClass()")
    public Object doLog(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (isNotRequestScope()) {
            return joinPoint.proceed();
        }

        final String className = findClassSimpleName(joinPoint);
        final String methodName = findMethodName(joinPoint);

        final TraceStatus status = logProcessor.begin(className, methodName, joinPoint.getArgs());

        try {
            final Object result = joinPoint.proceed();

            logProcessor.end(status, className, methodName, result);
            return result;
        } catch (final Throwable ex) {
            logProcessor.exception(status, className, methodName, ex);

            throw ex;
        }
    }

    private boolean isNotRequestScope() {
        return RequestContextHolder.getRequestAttributes() == null;
    }

    private String findClassSimpleName(final ProceedingJoinPoint joinPoint) {
        final Class<?> clazz = joinPoint.getTarget().getClass();

        return clazz.getSimpleName();
    }

    private String findMethodName(final ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }
}
