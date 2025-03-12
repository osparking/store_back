package com.bumsoap.store.security.user;

import com.bumsoap.store.repository.UserRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class BsUserDetailsService implements UserDetailsService {
    private final UserRepoI userRepo;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return null;
    }
}
