package com.ddang.ddang.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Attributes.Attribute;

@TestConfiguration
public class RestDocsConfiguration {

    public static Attribute field(String key, String value) {
        return new Attribute(key, value);
    }

    @Bean
    public RestDocumentationResultHandler write() {
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}",
                Preprocessors.preprocessRequest(
                        Preprocessors.modifyHeaders()
                                     .remove("Content-Length")
                                     .remove("Host"),
                        Preprocessors.prettyPrint()
                ),
                Preprocessors.preprocessResponse(
                        Preprocessors.modifyHeaders()
                                     .remove("Content-Length")
                                     .remove("Transfer-Encoding")
                                     .remove("Date")
                                     .remove("Keep-Alive")
                                     .remove("Connection"),
                        Preprocessors.prettyPrint()
                )
        );
    }
}
