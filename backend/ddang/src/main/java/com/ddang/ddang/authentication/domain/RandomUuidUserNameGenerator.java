package com.ddang.ddang.authentication.domain;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RandomUuidUserNameGenerator implements UserNameGenerator {

    private static final int NAME_LENGTH = 10;

    @Override
    public String generate() {
        return UUID.randomUUID().toString().substring(0, NAME_LENGTH);
    }
}
