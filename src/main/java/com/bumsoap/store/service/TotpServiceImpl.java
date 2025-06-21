package com.bumsoap.store.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

@Service
public class TotpServiceImpl implements TotpService {
  private final GoogleAuthenticator googleAuthenticator;

  public TotpServiceImpl(GoogleAuthenticator googleAuthenticator) {
    this.googleAuthenticator = googleAuthenticator;
  }

  @Override
  public GoogleAuthenticatorKey generateSecret() {
    return googleAuthenticator.createCredentials();
  }
}
