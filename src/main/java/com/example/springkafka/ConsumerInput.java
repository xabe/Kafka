package com.example.springkafka;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.messaging.Message;

public class ConsumerInput implements Consumer<Message<String>> {

  private final Logger logger;

  private final RestService restService;

  private final BindingsLifecycleController bindingsLifecycleController;

  public ConsumerInput(final RestService restService, BindingsLifecycleController bindingsLifecycleController) {
    this.restService = restService;
    this.logger = LoggerFactory.getLogger(ConsumerInput.class);
    this.bindingsLifecycleController = bindingsLifecycleController;
  }

  @Override
  public void accept(Message<String> message) {
    this.logger.info("Consumer event: {} ", message);
    try {
      this.restService.call(message.getPayload());
    } catch (final Exception e) {
      this.logger.error("Error Consumer event: {} ", message);
      bindingsLifecycleController.pause("consumer-in-0");
      this.logger.info("Paused Consumer event: {} ", message);
    }
  }

}
