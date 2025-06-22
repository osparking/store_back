package com.bumsoap.store.util;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.security.user.BsUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
  @Autowired
  UserRepoI userRepo;

  public Long loggedInUserId() {
    var principal = SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Long id = null;
    if (principal instanceof BsUserDetails) {
      id = ((BsUserDetails) principal).getId();
    } else if (principal instanceof DefaultOAuth2User) {
      String email = ((DefaultOAuth2User) principal).getAttribute("email");
      BsUser user = userRepo.findByEmail(email).orElseThrow(
          () -> new RuntimeException(Feedback.NOT_FOUND_EMAIL + email));
      id = user.getId();
    }
    return id;
  }
}
