package com.ddang.ddang.configuration.fcm;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@EqualsAndHashCode(callSuper = false)
public class MockGoogleCredentials extends GoogleCredentials {

    private final String tokenValue;
    private final long expiryTime;

    public MockGoogleCredentials(String tokenValue) {
        this(tokenValue, System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
    }

    public MockGoogleCredentials(String tokenValue, long expiryTime) {
        this.tokenValue = tokenValue;
        this.expiryTime = expiryTime;
    }

    @Override
    public AccessToken refreshAccessToken() {
        return new AccessToken(tokenValue, new Date(expiryTime));
    }
}
