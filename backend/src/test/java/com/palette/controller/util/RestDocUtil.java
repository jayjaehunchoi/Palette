package com.palette.controller.util;

import capital.scalable.restdocs.AutoDocumentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.palette.utils.advice.PaletteExceptionHandler;
import com.palette.utils.advice.ValidExceptionHandler;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsWebTestClientConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.operation.preprocess.ContentModifyingOperationPreprocessor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;


import static capital.scalable.restdocs.SnippetRegistry.*;
import static capital.scalable.restdocs.jackson.JacksonResultHandlers.prepareJackson;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

public class RestDocUtil {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Snippet[] SUCCESS_SNIPPETS = new Snippet[]{
            HttpDocumentation.httpRequest(),
            HttpDocumentation.httpResponse(),
            AutoDocumentation.requestFields(),
            AutoDocumentation.responseFields(),
            AutoDocumentation.pathParameters(),
            AutoDocumentation.requestParameters(),
            AutoDocumentation.description(),
            AutoDocumentation.methodAndPath(),
            AutoDocumentation.sectionBuilder().snippetNames(
                    AUTO_METHOD_PATH,
                    AUTO_DESCRIPTION,
                    AUTO_AUTHORIZATION,
                    AUTO_PATH_PARAMETERS,
                    AUTO_MODELATTRIBUTE,
                    AUTO_REQUEST_PARAMETERS,
                    AUTO_REQUEST_FIELDS,
                    AUTO_RESPONSE_FIELDS,
                    HTTP_REQUEST,
                    HTTP_RESPONSE
            ).skipEmpty(true).build()};

    public static MockMvc successRestDocsMockMvc(RestDocumentationContextProvider provider, Object... controllers) {
        return restDocsMockMvc(provider, SUCCESS_SNIPPETS, controllers);
    }

    public static MockMvc restDocsMockMvc(RestDocumentationContextProvider provider, Snippet[] snippets, Object... controllers) {
        return MockMvcBuilders.standaloneSetup(controllers)
                .addFilters(MockMvcConfig.utf8Filter())
                .alwaysDo(prepareJackson(OBJECT_MAPPER))
                .setControllerAdvice(PaletteExceptionHandler.class, ValidExceptionHandler.class)
                .apply(documentationConfiguration(provider)
                        .uris()
                        .withScheme("http")
                        .withHost("localhost")
                        .withPort(8080)
                        .and()
                        .snippets()
                        .withDefaults(snippets))
                .build();
    }

    @TestConfiguration
    public static class MockMvcConfig {

        @Bean
        public static ContentModifyingOperationPreprocessor prettyPrintPreProcessor() {
            return new ContentModifyingOperationPreprocessor(prettyPrintingUtils());
        }

        @Bean
        public static PrettyPrintingUtils prettyPrintingUtils() {
            return new PrettyPrintingUtils();
        }

        @Bean
        public static CharacterEncodingFilter utf8Filter() {
            return new CharacterEncodingFilter("UTF-8", true);
        }
    }
}
