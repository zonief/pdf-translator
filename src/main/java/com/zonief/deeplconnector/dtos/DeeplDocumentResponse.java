package com.zonief.deeplconnector.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeeplDocumentResponse {

  @JsonProperty("document_id")
  private String documentId;
  @JsonProperty("document_key")
  private String key;
}
