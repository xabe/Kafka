package com.example.springkafka.config;

import static org.mockito.Mockito.mock;

import com.example.springkafka.RestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  public RestService restService() {
    return mock(RestService.class);
  }
}
