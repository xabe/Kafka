package com.example.springkafka;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;

public class ConsumerResumeListener implements ApplicationListener<ListenerContainerIdleEvent> {

  public static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

  private final Logger logger;

  public ConsumerResumeListener() {
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Override
  public void onApplicationEvent(final ListenerContainerIdleEvent event) {
    this.logger.info("Received event {}", event);
    if (!event.getConsumer().paused().isEmpty()) {
      this.logger.info("Resume partitions {}", event);
      event.getConsumer().resume(event.getConsumer().paused());
      COUNT_DOWN_LATCH.countDown();
    }
  }

}
