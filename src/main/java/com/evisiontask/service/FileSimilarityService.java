package com.evisiontask.service;

import com.evisiontask.Dto.FileSimilarityAnalyze;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class FileSimilarityService {

    public List<FileSimilarityAnalyze> analyzeSimilarity(String AFilePath, String directoryPath) {

        Map<String,Long> mp = readAFileAndStoringInMap(AFilePath);

        Map<String,Map<String,Long>> poolFilesMp = extractFilesFromDirectoryThenStoringInMap(directoryPath);

        Map<String, FileSimilarityAnalyze> buildCalcScoreForAllFiles = buildCalcScoreForAllFiles(mp,poolFilesMp);

        buildCalcScoreForAllFiles.forEach((filePath,v) -> v.setFilePath(filePath));

        return buildCalcScoreForAllFiles.values().stream().toList();
    }

    private FileSimilarityAnalyze calculateSimilarity(Map<String,Long> fileA, Map<String,Long> poolFile) {

        long numberOfExtraWords = 0L;
        long numberOfMissingWords = 0L;
        long numberOfMatchingWords = 0L;


        for (Map.Entry<String, Long> entry : poolFile.entrySet()) {
            Long cnt = fileA.getOrDefault(entry.getKey(),0L);
            if(entry.getValue() > cnt) {
                numberOfExtraWords += entry.getValue() - cnt;
            }else if(entry.getValue() < cnt) {
                numberOfMissingWords += cnt - entry.getValue();
            }
            numberOfMatchingWords += Math.min(cnt,entry.getValue());
        }

        // get the word that existing in the file A and not existing in the pool file
        for (Map.Entry<String, Long> entry : fileA.entrySet()) {
            Long cnt = poolFile.getOrDefault(entry.getKey(),0L);
            if(cnt == 0) {
                numberOfMissingWords += entry.getValue();
            }
        }


        return new FileSimilarityAnalyze(numberOfExtraWords,numberOfMissingWords,numberOfMatchingWords);

    }

    private Map<String, FileSimilarityAnalyze> buildCalcScoreForAllFiles(Map<String,Long> fileA, Map<String,Map<String,Long>>  poolFiles) {

        Map<String, FileSimilarityAnalyze> result = new HashMap<>();

        for (Map.Entry<String, Map<String, Long>> entry : poolFiles.entrySet()) {

            result.put(entry.getKey(),calculateSimilarity(fileA,entry.getValue()));
        }

        return result;
    }

    private Map<String,Long> readAFileAndStoringInMap(String path) {
        File A = new File(path);
        if(!A.exists()) {
            throw new IllegalArgumentException("File does not exist in path : " + path);
        }
        return buildWordFrequencyMap(A);
    }

    private Map<String,Map<String,Long>> extractFilesFromDirectoryThenStoringInMap(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();

        Map<String,Map<String,Long>> result = new HashMap<>();

        for (File file : files) {
            result.put(file.getAbsolutePath(), buildWordFrequencyMap(file));
        }
        return result;
    }

    private Map<String, Long> buildWordFrequencyMap(File file) {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            return reader.lines()
                    .parallel()
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .filter(word -> !word.isEmpty() && isChunkWord(word))
                    .collect(groupingBy(identity(), counting()));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private boolean isChunkWord(String word) {

        for(int i =0 ;i<word.length();i++) {
            char c = word.charAt(i);
            if((c >= 'a' && c<='z')) {
                continue;
            }
            if((c >= 'A' && c<='Z')) {
                continue;
            }
            return false;
        }
        return true;
    }

}
