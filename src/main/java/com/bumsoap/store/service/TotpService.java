package com.bumsoap.store.service;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public interface TotpService {
  GoogleAuthenticatorKey generateSecret();
}
