package com.zonief.deeplconnector.config;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
@ToString
public class WebclientConfig {

  @Value("${webclient.apikey}")
  private String apiKey;

  @Value("${webclient.url}")
  private String url;


  @Bean(name = "deeplWebClient")
  public WebClient deeplWebClient() {
    log.debug(this.toString());
    final int size = 1024 * 1024;
    final ExchangeStrategies strategies =
        ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
            .build();
    return WebClient.builder()
        .defaultHeaders(httpHeaders -> httpHeaders.addAll(createHeaders()))
        .baseUrl(url)
        .exchangeStrategies(strategies)
        .build();
  }

  private HttpHeaders createHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "DeepL-Auth-Key " + apiKey);
    headers.add("User-Agent", "DeepL-Connector");
    return headers;
  }

}
