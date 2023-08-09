package com.ddang.ddang.authentication.configuration;

import com.ddang.ddang.authentication.configuration.exception.UserUnauthorizedException;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationStore store;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthenticationUserInfo.class) &&
                parameter.hasParameterAnnotation(AuthenticateUser.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final AuthenticationUserInfo userInfo = store.get();

        if (isInvalidUserPrincipal(userInfo, parameter)) {
            throw new UserUnauthorizedException("로그인이 필요한 기능입니다.");
        }

        return userInfo;
    }

    private boolean isInvalidUserPrincipal(final AuthenticationUserInfo userInfo, final MethodParameter parameter) {
        return userInfo == null ||
                userInfo.userId() == null && parameter.getParameterAnnotation(AuthenticateUser.class).required();
    }
}
