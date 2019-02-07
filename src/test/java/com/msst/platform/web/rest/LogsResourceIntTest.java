package com.msst.platform.web.rest;

import com.msst.platform.MsstPlatformApp;
import com.msst.platform.web.rest.vm.LoggerVM;
import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the LogsResource REST controller.
 *
 * @see LogsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsstPlatformApp.class)
public class LogsResourceIntTest {

    private WebTestClient webTestClient;

    @Before
    public void setup() {
        LogsResource logsResource = new LogsResource();
        this.webTestClient = WebTestClient
            .bindToController(logsResource)
            .build();
    }

    @Test
    public void getAllLogs() throws Exception {
        webTestClient.get().uri("/management/logs")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    public void changeLogs() throws Exception {
        LoggerVM logger = new LoggerVM();
        logger.setLevel("INFO");
        logger.setName("ROOT");

        webTestClient.put().uri("/management/logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .syncBody(TestUtil.convertObjectToJsonBytes(logger))
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    public void testLogstashAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        assertThat(context.getLogger("ROOT").getAppender("ASYNC_LOGSTASH")).isInstanceOf(AsyncAppender.class);
    }
}
