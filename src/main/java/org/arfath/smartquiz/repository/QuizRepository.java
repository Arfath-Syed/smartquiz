package org.arfath.smartquiz.repository;

import org.arfath.smartquiz.model.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Integer> {
}
