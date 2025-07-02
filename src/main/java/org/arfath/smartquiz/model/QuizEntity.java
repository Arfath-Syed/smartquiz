package org.arfath.smartquiz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quizzes")
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private String optionA;
    @Column(nullable = false)
    private String optionB;
    @Column(nullable = false)
    private String optionC;
    @Column(nullable = false)
    private String optionD;
    @Column(nullable = false)
    private String correctAnswer;

}
