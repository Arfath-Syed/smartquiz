package org.arfath.smartquiz.controller;

import io.jsonwebtoken.lang.Collections;
import org.arfath.smartquiz.dto.QuizAttemptHistoryDto;
import org.arfath.smartquiz.dto.QuizAttemptRequest;
import org.arfath.smartquiz.dto.QuizResultResponse;
import org.arfath.smartquiz.model.QuizAttemptEntity;
import org.arfath.smartquiz.model.QuizEntity;
import org.arfath.smartquiz.dto.QuizAttemptDto;
import org.arfath.smartquiz.service.AiQuizService;
import org.arfath.smartquiz.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final AiQuizService aiQuizService;

    public QuizController(QuizService quizService, AiQuizService aiQuizService) {
        this.quizService = quizService;
        this.aiQuizService = aiQuizService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/add")
    public ResponseEntity<QuizEntity> addQuiz(@RequestBody QuizEntity quiz){
        return ResponseEntity.ok(quizService.addQuiz(quiz));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<QuizEntity>> getAllQuizzesAdmin(){
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//    @GetMapping("/user/all")
//    public ResponseEntity<List<QuizAttemptDto>> getAllQuizzesUser(){
//        return ResponseEntity.ok(quizService.getQuizzesForAttempt());
//    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user/submit")
    public ResponseEntity<QuizResultResponse> submit(@RequestBody List<QuizAttemptRequest> attemptRequests){
        return ResponseEntity.ok(quizService.evaluateQuiz(attemptRequests));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/user/history")
    public ResponseEntity<List<QuizAttemptHistoryDto>> getUserHistory(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(quizService.getAllAttemptsForUser(username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/history")
    public ResponseEntity<List<QuizAttemptEntity>> getAdminHistory(){
        return ResponseEntity.ok(quizService.getAllAttemptsForAdmin());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/generate")
    public ResponseEntity<QuizEntity> generateQuiz(@RequestParam String topic) {
        return ResponseEntity.ok(aiQuizService.generateAndSaveQuiz(topic));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/approve/{quizId}")
    public ResponseEntity<String> approveQuiz(@PathVariable Integer quizId) {
        return ResponseEntity.ok(quizService.approveQuiz(quizId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/generateBatch")
    public ResponseEntity<List<QuizEntity>> generateBatch(
            @RequestParam String topic,
            @RequestParam(defaultValue = "5") int count) {

        if (count < 1 || count > 20) {
            return ResponseEntity.badRequest().body(Collections.emptyList());  // Safety: prevent abuse
        }


        return ResponseEntity.ok(quizService.generateBatch(topic, count));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/topics")
    public List<String> getAvailableTopics(){
        return quizService.getAvailableTopics();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/user/all")
    public List<QuizAttemptDto> getQuizzesByTopic(@RequestParam String topic){
        return quizService.getAppovedQuizzesByTopic(topic);
    }


}
