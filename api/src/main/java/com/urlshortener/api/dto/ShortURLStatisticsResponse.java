package com.urlshortener.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortURLStatisticsResponse {

    private String shortUrl;
    private String originalUrl;
    private Instant creation;
    private Long hits;
    private Instant lastHit;

}
