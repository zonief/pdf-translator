package com.zonief.deeplconnector.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class DeeplStatusResponse {

  @JsonProperty("document_id")
  private String id;
  private String status;
  @JsonProperty("seconds_remaining")
  private int secondsRemaining;

}
