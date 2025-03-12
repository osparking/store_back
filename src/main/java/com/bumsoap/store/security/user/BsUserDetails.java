package com.bumsoap.store.security.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class BsUserDetails {
    private Long id;
    private String email;
    private String password;
    private boolean usable;

    private Collection<GrantedAuthority> authorities;
}
