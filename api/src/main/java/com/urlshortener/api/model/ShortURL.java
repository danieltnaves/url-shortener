package com.urlshortener.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "short_url")
public class ShortURL {

    @Id
    private String id;

    private Instant creationDate;

    private String originalURL;

    private Long hits;

    private Instant lastHit;

    public ShortURL(Instant creationDate, String originalURL, Long hits) {
        this.creationDate = creationDate;
        this.originalURL = originalURL;
        this.hits = hits;
    }
}
