package com.urlshortener.api.repository;

import com.urlshortener.api.model.KeyStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyRepository extends MongoRepository<KeyStorage, String> {

    @Query("{ status : 'ACTIVE' }")
    Page<KeyStorage> getActiveKeys(Pageable pageable);

}
