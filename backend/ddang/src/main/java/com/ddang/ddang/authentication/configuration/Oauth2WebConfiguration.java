package com.ddang.ddang.authentication.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Oauth2WebConfiguration implements WebMvcConfigurer {

    private final Oauth2TypeConverter oauth2TypeConverter;
    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthenticationPrincipalArgumentResolver authenticationArgumentResolver;

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(oauth2TypeConverter);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/oauth2/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }
}
