package com.ddang.ddang.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {

    private static final String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Bean
    public Module module() {
        final SimpleModule module = new SimpleModule();

        module.addSerializer(LocalDateTime.class, new JsonSerializer<>() {

            @Override
            public void serialize(
                    final LocalDateTime value,
                    final JsonGenerator gen,
                    final SerializerProvider serializers
            ) throws IOException {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);

                gen.writeString(formatter.format(value));
            }
        });

        return module;
    }
}
