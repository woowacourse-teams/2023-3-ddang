package com.ddang.ddang.auction.configuration.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ClosingTimeValidator implements ConstraintValidator<ClosingTimeLimit, LocalDateTime> {

    private static final int MAXIMUM_CLOSING_TIME_DAYS = 30;

    @Override
    public boolean isValid(final LocalDateTime target, final ConstraintValidatorContext context) {
        if (target == null) {
            return false;
        }

        final long days = ChronoUnit.DAYS.between(LocalDateTime.now(), target);

        return days <= MAXIMUM_CLOSING_TIME_DAYS;
    }
}
