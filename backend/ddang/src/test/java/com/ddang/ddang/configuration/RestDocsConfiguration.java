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
                        Preprocessors.removeHeaders("Content-Length", "Host"),
                        Preprocessors.prettyPrint()
                ),
                Preprocessors.preprocessResponse(
                        Preprocessors.removeHeaders(
                                "Transfer-Encoding",
                                "Date",
                                "Keep-Alive",
                                "Connection"
                        ),
                        Preprocessors.prettyPrint()
                )
        );
    }
}
