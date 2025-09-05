package com.evisiontask.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileSimilarityAnalyze {

    private String filePath;
    private Long extraWords;
    private Long missingWords;
    private Long matchingWords;
    private double score;


    public FileSimilarityAnalyze(Long extraWords, Long missingWords, Long matchingWords) {
        this.extraWords = extraWords;
        this.missingWords = missingWords;
        this.matchingWords = matchingWords;
    }

    public double getScore() {
        long totalWords = extraWords + missingWords + matchingWords;
        double score =  totalWords == 0 ? 0.0 : (double) matchingWords / totalWords * 100;
        return Math.round(score * 100.0) / 100.0;
    }

}
