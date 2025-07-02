package org.arfath.smartquiz.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.arfath.smartquiz.model.QuizAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttemptEntity, Integer> {
    List<QuizAttemptEntity> findByUsername(String username);
}
