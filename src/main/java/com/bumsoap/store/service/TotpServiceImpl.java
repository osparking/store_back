package com.bumsoap.store.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
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

  @Override
  public String getQRcodeUrl(
      GoogleAuthenticatorKey secret, String username) {
    return GoogleAuthenticatorQRGenerator.getOtpAuthURL(
        "범이비누", username, secret);
  }

  @Override
  public boolean verifyCode(String secret, int code) {
    return googleAuthenticator.authorize(secret, code);
  }
}
