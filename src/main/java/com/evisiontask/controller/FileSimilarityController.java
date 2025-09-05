package com.evisiontask.controller;


import com.evisiontask.Dto.FileSimilarityAnalyze;
import com.evisiontask.service.FileSimilarityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/file-similarity")
@RequiredArgsConstructor
public class FileSimilarityController {

    private final FileSimilarityService fileSimilarityService;

    @GetMapping("/analyze")
    public ResponseEntity<?> analyzeSimilarity(@RequestParam String AFilePath, @RequestParam String directoryPath) {
        List<FileSimilarityAnalyze> results = fileSimilarityService.analyzeSimilarity(AFilePath,directoryPath);
        return ResponseEntity.ok(results);
    }
}
