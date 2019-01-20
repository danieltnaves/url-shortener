package com.urlshortener.keygenerator.keygenerator.config;

import com.urlshortener.keygenerator.keygenerator.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class SchedulerConfig {

  @Autowired private KeyService keyService;

  @Scheduled(fixedRate = 5000)
  public void createKeys() {
    keyService.saveKeyChunk();
  }
}
