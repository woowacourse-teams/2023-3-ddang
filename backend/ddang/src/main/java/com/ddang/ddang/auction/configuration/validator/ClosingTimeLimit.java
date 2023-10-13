package com.ddang.ddang.auction.configuration.validator;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClosingTimeValidator.class)
public @interface ClosingTimeLimit {

    String message() default "마감 시간은 현재 일자로부터 최대 30일까지 설정할 수 있습니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
