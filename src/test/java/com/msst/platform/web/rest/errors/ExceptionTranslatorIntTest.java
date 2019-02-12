package com.msst.platform.web.rest.errors;

import com.msst.platform.MsstPlatformApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.MockServerConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import java.util.List;

/**
 * Test class for the ExceptionTranslator controller advice.
 *
 * @see ExceptionTranslator
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsstPlatformApp.class)
public class ExceptionTranslatorIntTest {

    @Autowired
    private ExceptionTranslatorTestController controller;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private CodecCustomizer jacksonCodecCustomizer;

    @Autowired
    private List<WebExceptionHandler> exceptionHandlers;

    private WebTestClient webTestClient;

    @Before
    public void setup() {
        webTestClient = WebTestClient.bindToController(controller)
            .controllerAdvice(exceptionTranslator)
            .httpMessageCodecs(jacksonCodecCustomizer::customize)
            .apply(new MockServerConfigurer() {
                @Override
                public void beforeServerCreated(WebHttpHandlerBuilder builder) {
                    builder
                        .exceptionHandlers(List::clear)
                        .exceptionHandlers(handlers -> handlers.addAll(exceptionHandlers));
                }
            })
            .build();
    }

    @Test
    public void testConcurrencyFailure() {
        webTestClient.get().uri("/test/concurrency-failure")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT)
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo(ErrorConstants.ERR_CONCURRENCY_FAILURE);
    }

    @Test
    public void testMethodArgumentNotValid() {
         webTestClient.post().uri("/test/method-argument")
             .contentType(MediaType.APPLICATION_JSON)
             .syncBody("{}")
             .exchange()
             .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
             .expectBody()
             .jsonPath("$.message").isEqualTo(ErrorConstants.ERR_VALIDATION)
             .jsonPath("$.fieldErrors.[0].objectName").isEqualTo("testDTO")
             .jsonPath("$.fieldErrors.[0].field").isEqualTo("test")
             .jsonPath("$.fieldErrors.[0].message").isEqualTo("NotNull");
    }

    @Test
    public void testParameterizedError() {
        webTestClient.get().uri("/test/parameterized-error")
            .exchange()
            .expectStatus().isBadRequest()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("test parameterized error")
            .jsonPath("$.params.param0").isEqualTo("param0_value")
            .jsonPath("$.params.param1").isEqualTo("param1_value");
    }

    @Test
    public void testParameterizedError2() {
        webTestClient.get().uri("/test/parameterized-error2")
            .exchange()
            .expectStatus().isBadRequest()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("test parameterized error")
            .jsonPath("$.params.foo").isEqualTo("foo_value")
            .jsonPath("$.params.bar").isEqualTo("bar_value");
    }

    @Test
    public void testMissingRequestPart() {
        webTestClient.get().uri("/test/missing-servlet-request-part")
            .exchange()
            .expectStatus().isBadRequest()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.400");
    }

    @Test
    public void testMissingRequestParameter() {
        webTestClient.get().uri("/test/missing-servlet-request-parameter")
            .exchange()
            .expectStatus().isBadRequest()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.400");
    }

    @Test
    public void testAccessDenied() {
        webTestClient.get().uri("/test/access-denied")
            .exchange()
            .expectStatus().isForbidden()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.403")
            .jsonPath("$.detail").isEqualTo("test access denied!");
    }

    @Test
    public void testUnauthorized() {
        webTestClient.get().uri("/test/unauthorized")
            .exchange()
            .expectStatus().isUnauthorized()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.401")
            .jsonPath("$.path").isEqualTo("/test/unauthorized")
            .jsonPath("$.detail").isEqualTo("test authentication failed!");
    }

    @Test
    public void testMethodNotSupported() {
        webTestClient.post().uri("/test/access-denied")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.405")
            .jsonPath("$.detail").isEqualTo("Response status 405 with reason \"Request method 'POST' not supported\"");
    }

    @Test
    public void testExceptionWithResponseStatus() {
        webTestClient.get().uri("/test/response-status")
            .exchange()
            .expectStatus().isBadRequest()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.400")
            .jsonPath("$.title").isEqualTo("test response status");
    }

    @Test
    public void testInternalServerError() {
        webTestClient.get().uri("/test/internal-server-error")
            .exchange()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.500")
            .jsonPath("$.title").isEqualTo("Internal Server Error");
    }

}
