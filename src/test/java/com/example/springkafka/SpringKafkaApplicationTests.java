package com.example.springkafka;

import static java.util.concurrent.TimeUnit.SECONDS;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = SpringKafkaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class SpringKafkaApplicationTests {

  @Autowired
  private StreamBridge streamBridge;

  @Autowired
  private RestService restService;

  @Test
  public void shouldPauseConsumerAndResume() throws Exception {

    when(this.restService.call(any())).thenThrow(RuntimeException.class).thenReturn("ok");

    this.streamBridge.send("output-in-0",MessageBuilder.withPayload("message").build());

    ConsumerResumeListener.COUNT_DOWN_LATCH.await(10, SECONDS);

    this.streamBridge.send("output-in-0",MessageBuilder.withPayload("message").build());

    Awaitility.await().pollDelay(5, SECONDS).pollInterval(1, SECONDS).atMost(10, SECONDS)
        .until(() -> {
          verify(this.restService, times(2)).call(any());
          return true;
        });
  }
}
