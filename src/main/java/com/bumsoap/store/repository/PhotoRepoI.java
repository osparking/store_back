package com.bumsoap.store.repository;

import com.bumsoap.store.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepoI extends JpaRepository<Photo, Long> {
}
