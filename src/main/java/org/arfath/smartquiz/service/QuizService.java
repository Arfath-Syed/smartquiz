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
    private final AiQuizService aiQuizService;

    public QuizService(QuizRepository quizRepository, QuizAttemptRepository quizAttemptRepository, AiQuizService aiQuizService) {
        this.quizRepository = quizRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.aiQuizService = aiQuizService;
    }


    public QuizEntity addQuiz(QuizEntity quiz) {
        return quizRepository.save(quiz);
    }


    public List<QuizEntity> getAllQuizzes() {
        return quizRepository.findAll();
    }



    public List<QuizAttemptDto> getQuizzesForAttempt() {
        return quizRepository.findByApprovedTrue().stream().map(q -> new QuizAttemptDto(
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


    public String approveQuiz( Integer quizId) {
        QuizEntity quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        quiz.setApproved(true);
        quizRepository.save(quiz);

        return "Quiz approved successfully";
    }

    public List<QuizEntity> generateBatch(
             String topic,
             int count) {


        List<QuizEntity> generatedQuizzes = aiQuizService.generateMultipleQuizzes(topic, count);
        return generatedQuizzes;
    }


    public List<String> getAvailableTopics() {
        return quizRepository.findDistinctApprovedTopics();
    }


    public List<QuizAttemptDto> getAppovedQuizzesByTopic(String topic) {
        return quizRepository.findByTopicAndApprovedTrue(topic)
                .stream()
                .map(q -> new QuizAttemptDto(
                        q.getId(),
                        q.getQuestion(),
                        q.getOptionA(),
                        q.getOptionB(),
                        q.getOptionC(),
                        q.getOptionD()
                )).toList();
    }
}