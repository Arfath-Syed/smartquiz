package org.arfath.smartquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResultResponse {

    private int totalQuestions;
    private int correctAnswers;
    private Double scorePercentage;
}
