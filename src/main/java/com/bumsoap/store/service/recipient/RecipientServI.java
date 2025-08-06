package com.bumsoap.store.service.recipient;

import com.bumsoap.store.model.Recipient;

public interface RecipientServI {
  Recipient save(Recipient recipient);

  Recipient findById(int id);
}
