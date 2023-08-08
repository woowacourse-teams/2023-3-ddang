package com.ddang.ddang.authentication.configuration;

import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Oauth2TypeConverter implements Converter<String, Oauth2Type> {

    @Override
    public Oauth2Type convert(final String typeName) {
        return Oauth2Type.from(typeName);
    }
}
