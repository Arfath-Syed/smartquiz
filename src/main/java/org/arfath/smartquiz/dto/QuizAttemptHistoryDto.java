package org.arfath.smartquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttemptHistoryDto {
        private Integer quizId;
        private String selectedAnswer;
        private boolean isCorrect;


}
