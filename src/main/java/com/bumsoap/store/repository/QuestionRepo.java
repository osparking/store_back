package com.bumsoap.store.repository;

import com.bumsoap.store.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Long> {
}
