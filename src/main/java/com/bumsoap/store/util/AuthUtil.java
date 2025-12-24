package com.bumsoap.store.util;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.security.user.BsUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class AuthUtil {
    @Autowired
    UserRepoI userRepo;

    public static String generateSignature(String timestamp,
                                           String webhookSecret) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            hmac.init(new SecretKeySpec(
                    webhookSecret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            ));
            byte[] digest = hmac.doFinal(
                    timestamp.getBytes(StandardCharsets.UTF_8)
            );

            // Convert to hex (lowercase)
            return bytesToHex(digest);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length()==1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

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
