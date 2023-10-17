package com.ddang.ddang.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Profile("test")
public class AwsConfiguration {

    @Bean
    public S3Client s3Client() {
        return Mockito.mock(S3Client.class);
    }
}
