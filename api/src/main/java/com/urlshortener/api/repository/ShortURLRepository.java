package com.urlshortener.api.repository;

import com.urlshortener.api.model.ShortURL;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortURLRepository extends MongoRepository<ShortURL, String> {

}
