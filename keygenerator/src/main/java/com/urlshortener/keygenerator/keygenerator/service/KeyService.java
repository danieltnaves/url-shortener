package com.urlshortener.keygenerator.keygenerator.service;

import com.urlshortener.keygenerator.keygenerator.model.KeyStatus;
import com.urlshortener.keygenerator.keygenerator.model.KeyStorage;
import com.urlshortener.keygenerator.keygenerator.repository.KeyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;

@Service
@Slf4j
public class KeyService {

  private KeyRepository keyRepository;

  @Value("${chunk.size:100}")
  private Integer chunkSize;

  @Autowired
  KeyService(KeyRepository keyRepository) {
    this.keyRepository = keyRepository;
  }

  private String generateKey() {
    return Base64.getEncoder()
        .encodeToString(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8))
        .substring(0, 7);
  }

  public void saveKeyChunk() {
    if (generateMoreKeys()) {
      log.info("m=saveKeyChunk, Generating key chunk with size {}", chunkSize);
      List<KeyStorage> keyStorageChunkList = new ArrayList<>();
      IntStream.rangeClosed(0, chunkSize)
          .forEach(
              i ->
                  keyStorageChunkList.add(
                      new KeyStorage(generateKey(), Instant.now(), KeyStatus.ACTIVE)));
      keyRepository.saveAll(keyStorageChunkList);
      log.info(
          "m=saveKeyChunk, KeyStorage chunk saved with the following keys: {}",
          keyStorageChunkList);
    } else {
      log.info(
          "m=saveKeyChunk, Is not necessary to generate more keys. Current available keys count are: {}",
          keyRepository.countActiveKeys());
    }
  }

  private boolean generateMoreKeys() {
    return keyRepository.countActiveKeys() < chunkSize * 10;
  }
}
