package com.bumsoap.store.service.recipient;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.Recipient;
import com.bumsoap.store.repository.RecipientRepoI;
import com.bumsoap.store.service.address.AddressBasisServI;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipientServ implements RecipientServI {
  private final RecipientRepoI recipientRepo;
  private final AddressBasisServI addressBasisServ;

  @Override
  public Recipient save(Recipient recipient) {
    var recipientInDB = recipientRepo.getIdenticalRecipient(
        recipient.getAddressBasis().getId(),
        recipient.getAddressDetail(),
        recipient.getDoroZbun().ordinal(),
        recipient.getFullName(),
        recipient.getMbPhone()
    );
    if (recipientInDB.isPresent()) {
      return recipientInDB.get();
    }
    var addrBasis = addressBasisServ
        .saveUpdate(recipient.getAddressBasis());
    recipient.setAddressBasis(addrBasis);
    return recipientRepo.save(recipient);
  }

  @Override
  public Recipient findById(long id) {
    return recipientRepo.findById(id).orElseThrow(
        () -> new IdNotFoundEx(Feedback.RECIP_ID_NOT_FOUND));
  }

  @Override
  public String deleteById(Long id) {
    return recipientRepo.findById(id)
        .map(recipient -> {
          recipientRepo.deleteById(id);
          return recipient.getFullName();
        })
        .orElseThrow(() ->
            new IdNotFoundEx(Feedback.RECIP_ID_NOT_FOUND));
  }
}
