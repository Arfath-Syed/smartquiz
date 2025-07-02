package org.arfath.smartquiz.repository;

import org.arfath.smartquiz.model.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Integer> {
    List<QuizEntity> findByApprovedTrue();

    @Query("SELECT DISTINCT q.topic FROM QuizEntity q WHERE q.approved = true")
    List<String> findDistinctApprovedTopics();

    List<QuizEntity> findByTopicAndApprovedTrue(String topic);
}
