package com.ddang.ddang.authentication.domain.dto;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationStore {

    private final ThreadLocal<AuthenticationUserInfo> threadLocalAuthenticationStore = new ThreadLocal<>();

    public void set(final AuthenticationUserInfo userInfo) {
        threadLocalAuthenticationStore.set(userInfo);
    }

    public AuthenticationUserInfo get() {
        return threadLocalAuthenticationStore.get();
    }

    public void remove() {
        threadLocalAuthenticationStore.remove();
    }
}
