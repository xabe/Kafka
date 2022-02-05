package com.example.springkafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ConsumerInput {

  private final Logger logger;

  private final RestService restService;

  @Autowired
  public ConsumerInput(final RestService restService) {
    this.restService = restService;
    this.logger = LoggerFactory.getLogger(ConsumerInput.class);
  }

  @StreamListener(KafkaConfig.INPUT)
  public void in(final String message, @Header(KafkaHeaders.CONSUMER) final Consumer consumer) {
    this.logger.info("Consumer event: {} ", message);
    try {
      this.restService.call(message);
    } catch (final Exception e) {
      this.logger.error("Error Consumer event: {} ", message);
      this.pausedConsumer(consumer);
      this.logger.info("Paused Consumer event: {} ", message);
    }
  }

  private void pausedConsumer(final Consumer consumer) {
    consumer.pause(consumer.assignment());
  }

}
