package com.bumsoap.store.repository;

import com.bumsoap.store.model.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipientRepoI extends JpaRepository<Recipient, Long> {
}
