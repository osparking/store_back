package com.bumsoap.store.service.recipient;

import com.bumsoap.store.model.Recipient;
import com.bumsoap.store.repository.RecipientRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipientServ implements RecipientServI {
  private final RecipientRepoI recipientRepo;

  @Override
  public Recipient save(Recipient recipient) {
    return recipientRepo.save(recipient);
  }

  @Override
  public Recipient findById(int id) {
    return null;
  }
}
