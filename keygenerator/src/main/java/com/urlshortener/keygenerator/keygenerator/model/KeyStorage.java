package com.urlshortener.keygenerator.keygenerator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "key_storage")
public class KeyStorage implements Serializable {

  @Id private String id;

  private Instant creation;

  private KeyStatus status;
}
