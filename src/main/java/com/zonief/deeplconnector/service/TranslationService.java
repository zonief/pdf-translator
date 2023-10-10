package com.zonief.deeplconnector.service;

import com.zonief.deeplconnector.dtos.DeeplDocumentResponse;
import com.zonief.deeplconnector.dtos.DeeplStatusResponse;
import java.io.File;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
@Slf4j
public class TranslationService {

  private final WebClient deeplWebClient;

  /**
   * Translates a file to the given language
   *
   * @param file     the file to translate
   * @param language the language to translate to
   * @return the translated file
   */
  public byte[] translateFile(File file, String language) {

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("target_lang", language);
    body.add("file", new FileSystemResource(file));

    //send the file to deepl
    DeeplDocumentResponse deeplDocumentResponse = deeplWebClient
        .post()
        .uri("/document")
        .header("Content-Type", "multipart/form-data")
        .bodyValue(body)
        .retrieve().bodyToMono(DeeplDocumentResponse.class).block();

    assert deeplDocumentResponse != null;
    statusPolling(deeplDocumentResponse);

    //download and return the file
    return deeplWebClient
        .post()
        .uri("/document/{documentId}/result", deeplDocumentResponse.getDocumentId())
        .bodyValue(deeplDocumentResponse)
        .retrieve().bodyToMono(byte[].class).block();
  }

  /**
   * Polls the status of the translation until it is done
   *
   * @param deeplDocumentResponse the response from deepl containing the document id
   */
  private void statusPolling(DeeplDocumentResponse deeplDocumentResponse) {
    String status = "";
    while (!status.equals("done")) {
      DeeplStatusResponse deeplStatusResponse = deeplWebClient
          .post()
          .uri("/document/{documentId}", deeplDocumentResponse.getDocumentId())
          .bodyValue(deeplDocumentResponse)
          .retrieve().bodyToMono(DeeplStatusResponse.class).block();
      assert deeplStatusResponse != null;
      log.debug("Status: {}", deeplStatusResponse);
      if (deeplStatusResponse.getStatus().equals("error")) {
        log.error("Error on Deepl side: {}", "An irrecoverable error occurred while translating the document");
        System.exit(1);
      }
      status = deeplStatusResponse.getStatus();
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        log.error("Error while waiting for translation", e);
        Thread.currentThread().interrupt();
      }
    }
  }

}
