package com.zonief.deeplconnector;

import com.zonief.deeplconnector.service.TranslationService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
@Slf4j
public class DeeplConnectorApplication implements CommandLineRunner {

  private final TranslationService translationService;

  public static void main(String[] args) {
    SpringApplication.run(DeeplConnectorApplication.class, args);
  }

  @Override
  public void run(String... args) {

    if (args.length == 2) {
      String language = args[1];

      //create a new file with the translated language in the name
      File translationFile = new File(
          StringUtils.substringBefore(args[0], ".pdf") + "-" + args[1].toUpperCase() + "-"
              + UUID.randomUUID()
              + ".pdf");

      //translate the file
      byte[] translation = translationService.translateFile(new File(args[0]), language);

      //write the file to disk
      try (FileOutputStream outputStream = new FileOutputStream(translationFile)) {
        outputStream.write(translation);
        log.info("File Written Successfully at {}", translationFile.getAbsolutePath());
      } catch (IOException e) {
        log.error("Error writing file", e);
      }

    } else {
      throw new IllegalArgumentException("You must provide a file path as argument");
    }
    System.exit(0);
  }

}
