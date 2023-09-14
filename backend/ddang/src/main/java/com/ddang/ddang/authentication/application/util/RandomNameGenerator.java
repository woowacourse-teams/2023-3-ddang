package com.ddang.ddang.authentication.application.util;

import java.util.Random;

public class RandomNameGenerator {

    private static final int NAME_LENGTH = 10;

    private static final Random random = new Random();

    private RandomNameGenerator() {
    }

    public static String generate() {
        StringBuilder name = new StringBuilder();

        for (int i = 0; i < NAME_LENGTH; i++) {
            int digit = random.nextInt(10);
            name.append(digit);
        }

        return name.toString();
    }
}
