package org.arfath.smartquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttemptRequest {
    private Integer quizId;
    private String selectedAnswer;
}
