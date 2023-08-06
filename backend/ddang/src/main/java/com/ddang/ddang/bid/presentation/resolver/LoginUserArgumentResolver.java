package com.ddang.ddang.bid.presentation.resolver;

import com.ddang.ddang.bid.presentation.dto.request.LoginUserRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginUserRequest.class) &&
                parameter.hasParameterAnnotation(LoginUser.class);
    }

    // TODO: 2023/07/28 3차 데모데이만을 위한 코드로 추후 리팩토링을 진행할 예정입니다
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

        return new LoginUserRequest(Long.parseLong(authorization));
    }
}
