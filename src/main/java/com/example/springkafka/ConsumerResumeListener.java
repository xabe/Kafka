package com.example.springkafka;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.stereotype.Component;

@Component
public class ConsumerResumeListener implements ApplicationListener<ListenerContainerIdleEvent> {

  public static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

  private final Logger logger;

  private final BindingsLifecycleController bindingsLifecycleController;

  public ConsumerResumeListener(ApplicationContext applicationContext) {
    this.logger = LoggerFactory.getLogger(this.getClass());
    this.bindingsLifecycleController = applicationContext.getBean(BindingsLifecycleController.class);
  }

  @Override
  public void onApplicationEvent(final ListenerContainerIdleEvent event) {
    this.logger.info("Resume partitions {}", event);
    if (event.isPaused() || !event.getConsumer().paused().isEmpty()) {
      this.logger.info("Resume partitions {}", event);
      final List<Map<?, ?>> maps = bindingsLifecycleController.queryStates();
      maps.forEach(map -> {
        if(Boolean.valueOf(map.get("paused").toString())){
          bindingsLifecycleController.resume((map.get("bindingName").toString()));
        }
      });
      COUNT_DOWN_LATCH.countDown();
    }
  }

}
