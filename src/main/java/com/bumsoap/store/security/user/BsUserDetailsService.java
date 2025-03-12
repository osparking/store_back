package com.bumsoap.store.security.user;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BsUserDetailsService implements UserDetailsService {
    private final UserRepoI userRepo;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        BsUser user = userRepo.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(
                        Feedback.NOT_FOUND_EMAIL + email));
        return BsUserDetails.buildUserDetails(user);
    }
}
