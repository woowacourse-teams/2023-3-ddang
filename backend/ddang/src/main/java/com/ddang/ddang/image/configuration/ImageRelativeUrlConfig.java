package com.ddang.ddang.image.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan
@EnableConfigurationProperties(ImageRelativeUrlConfigurationProperties.class)
public class ImageRelativeUrlConfig {

}
