package com.urlshortener.keygenerator.keygenerator.config;

import com.urlshortener.keygenerator.keygenerator.model.KeyStorage;
import com.urlshortener.keygenerator.keygenerator.service.KeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.util.List;

@Configuration
@Slf4j
public class SchedulerConfig {

  @Autowired private KeyService keyService;

  @Scheduled(fixedRate = 5000)
  public void createKeys() {
    List<KeyStorage> keyStorageList = keyService.saveKeyChunk();
    if (!StringUtils.isEmpty(keyStorageList)) {
      log.info("m=saveKeyChunk, KeyStorage chunk saved with the following keys: {}", keyStorageList);
    } else {
      log.info("m=saveKeyChunk, Is not necessary to generate more keys.");
    }
  }
}
