package com.urlshortener.api.service;

import com.urlshortener.api.model.KeyStatus;
import com.urlshortener.api.model.KeyStorage;
import com.urlshortener.api.repository.KeyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KeyService {

  private KeyRepository keyRepository;

  @Autowired
  public KeyService(KeyRepository keyRepository) {
    this.keyRepository = keyRepository;
  }

  public KeyStorage getOneActiveKey() {
    KeyStorage key =
        keyRepository.getActiveKeys(PageRequest.of(0, 1, Sort.by("creation"))).getContent().get(0);
    key.setStatus(KeyStatus.INACTIVE);
    return keyRepository.save(key);
  }
}
