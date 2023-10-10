# PDF Translator

## Description

This application translates the content of a PDF file into the desired language, creating a new file with the translation.

## Prerequisites

- Java (Version: 21)
- Maven (Version: 3.8.1)
- A Deepl API account with an API key that you have to set as an environment variable named `webclient.apikey` or replace the value in the application.yml file.

## Usage

The `run` method accepts two command-line arguments:
1. The path to the PDF file to be translated.
2. The target language for the translation.

It then creates a new PDF file with the translated content and names it using the original filename, target language, and a random UUID.

## How to Run

In your terminal, navigate to the project folder and execute the following command (replace `<file_path>` and `<target-language>` with actual values):

```shell
java -jar deepl-connector-0.0.1-SNAPSHOT.jar <file_path> <target-language>
