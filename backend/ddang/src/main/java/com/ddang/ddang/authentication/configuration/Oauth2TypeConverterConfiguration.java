package com.ddang.ddang.authentication.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class Oauth2TypeConverterConfiguration implements WebMvcConfigurer {

    private final Oauth2TypeConverter oauth2TypeConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(oauth2TypeConverter);
    }
}
