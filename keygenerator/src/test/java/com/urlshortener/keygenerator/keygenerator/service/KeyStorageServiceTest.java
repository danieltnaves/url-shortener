package com.urlshortener.keygenerator.keygenerator.service;

import com.urlshortener.keygenerator.keygenerator.model.KeyStatus;
import com.urlshortener.keygenerator.keygenerator.model.KeyStorage;
import com.urlshortener.keygenerator.keygenerator.repository.KeyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KeyStorageServiceTest {

  @Mock private KeyRepository keyRepository;

  private KeyService keyService;

  @Before
  public void beforeMethodCallInit() {
    keyService = new KeyService(keyRepository, 100);
  }

  @Test
  public void testIfKeyIsWithCorrectSize() {
    assertEquals(keyService.generateKey().length(), 7);
  }

  @Test
  public void testIfNeedToGenerateMoreKeys() {
    when(keyRepository.countActiveKeys()).thenReturn(10L);
    assertTrue(keyService.generateMoreKeys());
  }

  @Test
  public void testIfNotNeedToGenerateMoreKeys() {
    when(keyRepository.countActiveKeys()).thenReturn(1000000L);
    assertFalse(keyService.generateMoreKeys());
  }

  @Test
  public void testSaveKeyChunk() {
    when(keyRepository.countActiveKeys()).thenReturn(10L);
    when(keyRepository.saveAll(any(List.class)))
        .thenReturn(
            Arrays.asList(
                new KeyStorage("abc", Instant.now(), KeyStatus.ACTIVE),
                new KeyStorage("efg", Instant.now(), KeyStatus.ACTIVE),
                new KeyStorage("hij", Instant.now(), KeyStatus.ACTIVE),
                new KeyStorage("klm", Instant.now(), KeyStatus.ACTIVE),
                new KeyStorage("nop", Instant.now(), KeyStatus.ACTIVE)));
    assertEquals(keyService.saveKeyChunk().size(), 5);
  }

  @Test
  public void testSaveNoNeedMoreKeyChunk() {
    when(keyRepository.countActiveKeys()).thenReturn(1000000L);
    assertEquals(keyService.saveKeyChunk().size(), 0);
  }
}
