package org.arfath.smartquiz.controller;

import org.arfath.smartquiz.dto.QuizAttemptHistoryDto;
import org.arfath.smartquiz.dto.QuizAttemptRequest;
import org.arfath.smartquiz.dto.QuizResultResponse;
import org.arfath.smartquiz.model.QuizAttemptEntity;
import org.arfath.smartquiz.model.QuizEntity;
import org.arfath.smartquiz.dto.QuizAttemptDto;
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

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
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

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user/all")
    public ResponseEntity<List<QuizAttemptDto>> getAllQuizzesUser(){
        return ResponseEntity.ok(quizService.getQuizzesForAttempt());
    }

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

}
