package com.urlshortener.api.service;

import com.urlshortener.api.exception.BadRequestException;
import com.urlshortener.api.exception.NotFoundException;
import com.urlshortener.api.exception.UnvailableKeyException;
import com.urlshortener.api.messages.ExceptionMessages;
import com.urlshortener.api.model.KeyStatus;
import com.urlshortener.api.model.KeyStorage;
import com.urlshortener.api.model.ShortURL;
import com.urlshortener.api.repository.ShortURLRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class ShortenerService {

  private ShortURLRepository shortURLRepository;

  private KeyService keyService;

  private String host;

  private String contextPath;

  private String port;

  @Autowired
  public ShortenerService(
      KeyService keyService,
      ShortURLRepository shortURLRepository,
      @Value("${server.hostname}") String host,
      @Value("${server.contextPath}") String contextpath,
      @Value("${server.port}") String port) {
    this.shortURLRepository = shortURLRepository;
    this.keyService = keyService;
    this.host = host;
    this.contextPath = contextpath;
    this.port = port;
  }

  public ShortURL createNewShortedURL(ShortURL shortURL) {

    if (shortURL == null || shortURL.getOriginalURL().isEmpty()) {
      throw new BadRequestException(ExceptionMessages.EMPTY_EVENT);
    }

    try {
      new URL(shortURL.getOriginalURL());
    } catch (MalformedURLException e) {
      throw new BadRequestException(
          String.format("%s : %s", ExceptionMessages.INVALID_URL, shortURL.getOriginalURL()));
    }

    KeyStorage key = keyService.getOneActiveKey();

    if (StringUtils.isEmpty(key) || KeyStatus.ACTIVE.equals(key.getStatus())) {
      throw new UnvailableKeyException(ExceptionMessages.UNVAILABLE_KEYS);
    }

    shortURL.setId(key.getId());
    ShortURL createdURL = shortURLRepository.save(shortURL);
    log.info("m=createNewShortedURL, New short URL created, data: {}", createdURL);

    return createdURL;
  }

  public ShortURL getShortUrl(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new BadRequestException(ExceptionMessages.EMPTY_EVENT);
    }
    Optional<ShortURL> shortURL = shortURLRepository.findById(key);
    log.info("m=getShortUrl, Short URL value: {}", shortURL);
    ShortURL shortUrlReturned =
        shortURL.orElseThrow(
            () ->
                new NotFoundException(
                    String.format("%s %s", ExceptionMessages.KEY_NOT_FOUND, key)));
    updateStatistics(shortUrlReturned);
    return shortUrlReturned;
  }

  public ShortURL getShortUrlData(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new BadRequestException(ExceptionMessages.EMPTY_EVENT);
    }
    Optional<ShortURL> shortURL = shortURLRepository.findById(key);
    log.info("m=getShortUrl, Short URL value: {}", shortURL);
    return shortURL.orElseThrow(
        () -> new NotFoundException(String.format("%s %s", ExceptionMessages.KEY_NOT_FOUND, key)));
  }

  private void updateStatistics(ShortURL shortURL) {
    shortURL.setHits(shortURL.getHits() + 1);
    shortURL.setLastHit(Instant.now());
    shortURLRepository.save(shortURL);
  }

  public String mountShortenedUrl(String key) {
    return String.format("%s:%s%s/%s", host, port, contextPath, key);
  }
}
