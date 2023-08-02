package com.ddang.ddang.chat.presentation.auth;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthenticateUserInfo.class) &&
                parameter.hasParameterAnnotation(com.ddang.ddang.chat.presentation.auth.AuthenticateUser.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }

        return new AuthenticateUserInfo(Long.parseLong(authorization));
    }
}
