package com.urlshortener.api.service;

import com.urlshortener.api.model.KeyStatus;
import com.urlshortener.api.model.KeyStorage;
import com.urlshortener.api.repository.KeyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KeyServiceTest {

  private KeyService keyService;

  @Mock
  private KeyRepository keyRepository;

  @Before
  public void beforeMethodCallInit() {
    this.keyService = new KeyService(keyRepository);
  }

  @Test
  public void testGetOneActiveKey() {
    KeyStorage keyStorage = new KeyStorage("abc", Instant.now(), KeyStatus.ACTIVE);
    List<KeyStorage> keyStorageList = Arrays.asList(keyStorage);
    when(keyRepository.getActiveKeys(any())).thenReturn(new PageImpl<>(keyStorageList));
    keyStorage.setStatus(KeyStatus.INACTIVE);
    when(keyRepository.save(any())).thenReturn(keyStorage);
    KeyStorage oneActiveKey = keyService.getOneActiveKey();
    assertEquals("abc", oneActiveKey.getId());
    assertEquals(KeyStatus.INACTIVE, oneActiveKey.getStatus());
  }

}
