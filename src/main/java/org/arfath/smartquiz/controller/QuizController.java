package org.arfath.smartquiz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @GetMapping("/admin/all")
    public ResponseEntity<String> getAllQuizQuestions(){
        return ResponseEntity.ok("Admin: All Question");
    }

    @GetMapping("/user/attempt")
    public ResponseEntity<String> attemptQuiz(){
        return ResponseEntity.ok("User: Attempt Quiz");
    }

}
