package org.arfath.smartquiz.service;


import org.arfath.smartquiz.dto.AiQuizDto;
import org.arfath.smartquiz.model.QuizEntity;
import org.arfath.smartquiz.repository.QuizRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiQuizService {

    private final ChatClient chatClient;
    private final QuizRepository quizRepository;


    public AiQuizService(ChatClient chatClient, QuizRepository quizRepository) {
        this.chatClient = chatClient;
        this.quizRepository = quizRepository;
    }

    public QuizEntity generateAndSaveQuiz(String topic){

        String prompt = """
    Generate a multiple choice quiz question about: %s.
    Return strictly in valid JSON format with the following fields:
    
    - question: The quiz question
    - optionA: Option A
    - optionB: Option B
    - optionC: Option C
    - optionD: Option D
    - correctAnswer: The letter of the correct option (A, B, C, or D only)
    
    Do not include explanations or extra fields.
    Ensure the correctAnswer field only contains 'A', 'B', 'C', or 'D'.
    """.formatted(topic);


        AiQuizDto aiQuizDto = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(AiQuizDto.class);

        QuizEntity quiz = new QuizEntity();
        quiz.setQuestion(aiQuizDto.getQuestion());
        quiz.setOptionA(aiQuizDto.getOptionA());
        quiz.setOptionB(aiQuizDto.getOptionB());
        quiz.setOptionC(aiQuizDto.getOptionC());
        quiz.setOptionD(aiQuizDto.getOptionD());
        quiz.setCorrectAnswer(aiQuizDto.getCorrectAnswer());
        quiz.setTopic(topic);
        quiz.setApproved(false);

        return quizRepository.save(quiz);
    }

    public List<QuizEntity> generateMultipleQuizzes(String topic, int count) {
        List<QuizEntity> quizzes = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            QuizEntity quiz = generateAndSaveQuiz(topic);  // Reuse existing single quiz generation
            quizzes.add(quiz);
        }

        return quizzes;
    }

}
