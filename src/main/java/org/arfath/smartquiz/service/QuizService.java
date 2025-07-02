package org.arfath.smartquiz.service;

import org.arfath.smartquiz.dto.QuizAttemptHistoryDto;
import org.arfath.smartquiz.dto.QuizAttemptRequest;
import org.arfath.smartquiz.dto.QuizResultResponse;
import org.arfath.smartquiz.model.QuizAttemptEntity;
import org.arfath.smartquiz.model.QuizEntity;
import org.arfath.smartquiz.dto.QuizAttemptDto;
import org.arfath.smartquiz.repository.QuizAttemptRepository;
import org.arfath.smartquiz.repository.QuizRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    public QuizService(QuizRepository quizRepository, QuizAttemptRepository quizAttemptRepository) {
        this.quizRepository = quizRepository;
        this.quizAttemptRepository = quizAttemptRepository;
    }


    public QuizEntity addQuiz(QuizEntity quiz) {
        return quizRepository.save(quiz);
    }

    public List<QuizEntity> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public List<QuizAttemptDto> getQuizzesForAttempt() {
        return quizRepository.findAll().stream().map(q -> new QuizAttemptDto(
                q.getId(),
                q.getQuestion(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD()
        )).toList();
    }

    public QuizResultResponse evaluateQuiz(List<QuizAttemptRequest> attempts) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int total = attempts.size();
        int correct = 0;
        for (QuizAttemptRequest attempt : attempts) {
            QuizEntity quiz = quizRepository.findById(attempt.getQuizId()).orElse(null);
            boolean isCorrect = quiz != null && quiz.getCorrectAnswer().equalsIgnoreCase(attempt.getSelectedAnswer());

            if (isCorrect) correct++;

            QuizAttemptEntity attemptEntity =
                    new QuizAttemptEntity(
                            null,
                            username,
                            attempt.getQuizId(),
                            attempt.getSelectedAnswer(),
                            isCorrect
                    );
            quizAttemptRepository.save(attemptEntity);
        }

        double percentage = total > 0 ? (correct * 100.0) / total : 0;

        return new QuizResultResponse(total, correct, percentage);
    }

    public List<QuizAttemptHistoryDto> getAllAttemptsForUser(String username) {
        return quizAttemptRepository.findByUsername(username)
                .stream()
                .map(quizAttemptEntity ->
                        new QuizAttemptHistoryDto(
                                quizAttemptEntity.getQuizId(),
                                quizAttemptEntity.getSelectedAnswer(),
                                quizAttemptEntity.isCorrect()
                        )
                ).toList();

    }


    public List<QuizAttemptEntity> getAllAttemptsForAdmin() {
        return quizAttemptRepository.findAll();
    }

}