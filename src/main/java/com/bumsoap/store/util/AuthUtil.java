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

  public BsUserDetails loggedInUserDetails() {
    var principal = SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    BsUserDetails details = switch (principal) {
      case BsUserDetails userDetails -> userDetails;
      case DefaultOAuth2User oauth2User -> {
        String email = oauth2User.getAttribute("email");
        yield userRepo.findByEmail(email)
            .map(BsUserDetails::buildUserDetails)
            .orElseThrow(() -> new RuntimeException
                (Feedback.NOT_FOUND_EMAIL + email));
      }
      default -> null;
    };
    return details;
  }

  public Long loggedInUserId() {
    var principal = SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Long id = switch (principal) {
      case BsUserDetails userDetails -> userDetails.getId();
      case DefaultOAuth2User oauth2User -> {
        String email = oauth2User.getAttribute("email");
        yield userRepo.findByEmail(email)
            .map(BsUser::getId)
            .orElseThrow(() -> new RuntimeException
                (Feedback.NOT_FOUND_EMAIL + email));
      }
      default -> null;
    };
    return id;
  }
}
