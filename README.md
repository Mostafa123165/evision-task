# File Similarity API Documentation

## Overview
This project provides a RESTful API for analyzing the similarity between a reference file and a set of files in a directory. It is built with Spring Boot and Java 17.

## Features
- Compare a reference file to all files in a directory
- Returns similarity metrics: extra words, missing words, matching words, and a similarity score
- Easy integration via REST endpoint

## API Endpoint
### Analyze File Similarity
`GET /file-similarity/analyze`

#### Query Parameters
- `AFilePath` (string, required): Absolute path to the reference file
- `directoryPath` (string, required): Absolute path to the directory containing files to compare

#### Example Request
```
GET /file-similarity/analyze?AFilePath=/path/to/reference.txt&directoryPath=/path/to/directory
```

#### Example Response
```json
[
  {
    "filePath": "/path/to/directory/file1.txt",
    "extraWords": 10,
    "missingWords": 5,
    "matchingWords": 100,
    "score": 90.09
  },
  {
    "filePath": "/path/to/directory/file2.txt",
    "extraWords": 20,
    "missingWords": 10,
    "matchingWords": 80,
    "score": 76.19
  }
]
```

#### Response Fields
- `filePath`: Path to the compared file
- `extraWords`: Words present in the compared file but not in the reference file
- `missingWords`: Words present in the reference file but not in the compared file
- `matchingWords`: Words present in both files
- `score`: Similarity score (percentage of matching words)

## How It Works
1. The API reads the reference file and all files in the specified directory.
2. It counts word frequencies and compares them.
3. For each file, it calculates extra, missing, and matching words, then computes a similarity score.

## Project Structure
- `controller/FileSimilarityController.java`: REST API controller
- `service/FileSimilarityService.java`: Business logic for file comparison
- `Dto/FileSimilarityAnalyze.java`: DTO for response data

## Running the Project
1. Ensure you have Java 17 and Maven installed.
2. Build and run the project:
   ```bash
   mvn spring-boot:run
   ```
3. Access the API at `http://localhost:8080/file-similarity/analyze`
