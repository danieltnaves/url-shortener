package com.urlshortener.keygenerator.keygenerator.repository;

import com.urlshortener.keygenerator.keygenerator.model.KeyStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyRepository extends MongoRepository<KeyStorage, String> {

    @Query(value = "{ status : 'ACTIVE' }", count = true)
    Long countActiveKeys();

}
