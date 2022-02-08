package com.example.springkafka;

import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class StreamsConfig {

  @Bean("consumer")
 public ConsumerInput consumerInput(RestService restService, BindingsLifecycleController bindingsLifecycleController){
    return new ConsumerInput(restService, bindingsLifecycleController);
 }

}
