package com.ddang.ddang.authentication.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan
@EnableConfigurationProperties({JwtConfigurationProperties.class, KakaoProvidersConfigurationProperties.class})
public class Oauth2PropertiesConfiguration {
}
