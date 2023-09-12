package com.ddang.ddang.configuration.fcm;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final MockGoogleCredentials that = (MockGoogleCredentials) o;
        return expiryTime == that.expiryTime && Objects.equals(tokenValue, that.tokenValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tokenValue, expiryTime);
    }
}
