package com.urlshortener.api.service;

import com.urlshortener.api.exception.BadRequestException;
import com.urlshortener.api.exception.UnvailableKeyException;
import com.urlshortener.api.model.KeyStatus;
import com.urlshortener.api.model.KeyStorage;
import com.urlshortener.api.model.ShortURL;
import com.urlshortener.api.repository.ShortURLRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShortenerServiceTest {

  @Mock private KeyService keyService;

  @Mock private ShortURLRepository shortURLRepository;

  private ShortenerService shortenerService;

  @Before
  public void beforeMethodCallInit() {
    this.shortenerService =
        new ShortenerService(keyService, shortURLRepository, "http://myhost", "/shortener", "8080");
  }

  @Test(expected = BadRequestException.class)
  public void testIfEmptyObjectThrowsException() {
    shortenerService.createNewShortedURL(null);
  }

  @Test(expected = BadRequestException.class)
  public void testIfMalFormedUrlThrowsException() {
    shortenerService.createNewShortedURL(new ShortURL(Instant.now(), "wwwgoogle.com", 0L));
  }

  @Test(expected = UnvailableKeyException.class)
  public void testIfEmptyKeyThrowsException() {
    when(keyService.getOneActiveKey()).thenReturn(null);
    shortenerService.createNewShortedURL(new ShortURL(Instant.now(), "http://www.google.com", 0L));
  }

  @Test(expected = UnvailableKeyException.class)
  public void testIfActiveKeyThrowsException() {
    when(keyService.getOneActiveKey())
        .thenReturn(new KeyStorage("ABC", Instant.now(), KeyStatus.ACTIVE));
    shortenerService.createNewShortedURL(new ShortURL(Instant.now(), "http://www.google.com", 0L));
  }

  @Test
  public void testIfANewUrlWasCreatedWithSuccess() {
    KeyStorage keyStorage = new KeyStorage("ABC", Instant.now(), KeyStatus.INACTIVE);
    when(keyService.getOneActiveKey()).thenReturn(keyStorage);
    when(shortURLRepository.save(any()))
        .thenReturn(new ShortURL("ABC", Instant.now(), "http://google.com", 0L, Instant.now()));
    ShortURL newShortedURL =
        shortenerService.createNewShortedURL(
            new ShortURL(Instant.now(), "http://www.google.com", 0L));
    assertEquals(
        "http://myhost:8080/shortener/ABC",
        String.format("http://myhost:8080/shortener/%s", newShortedURL.getId()));
  }

  @Test
  public void testIfGetShortUrl() {
    ShortURL shortUrl = new ShortURL("abc", Instant.now(), "http://google.com", 0L, Instant.now());
    when(shortURLRepository.findById(any())).thenReturn(Optional.ofNullable(shortUrl));
    assertEquals("abc", shortenerService.getShortUrl("abc").getId());
  }

  @Test
  public void testIfGetShortUrlData() {
    ShortURL shortUrl = new ShortURL("abc", Instant.now(), "http://google.com", 0L, Instant.now());
    when(shortURLRepository.findById(any())).thenReturn(Optional.ofNullable(shortUrl));
    assertEquals("abc", shortenerService.getShortUrlData("abc").getId());
  }

  @Test
  public void testMountShortUrl() {
      assertEquals("http://myhost:8080/shortener/abc", shortenerService.mountShortenedUrl("abc"));
  }

}
