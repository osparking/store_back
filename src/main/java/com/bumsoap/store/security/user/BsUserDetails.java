package com.bumsoap.store.security.user;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.util.LoginSource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BsUserDetails implements UserDetails {
    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private String fullName;
    private boolean enabled;

    private Collection<GrantedAuthority> authorities;

    public String getSignUpMethod() {
        return LoginSource.valueOf(signUpMethod).getLabel();
    }

    private String signUpMethod;
    private String loginMethod;
    private boolean twoFaAEnabled;
    private String mbPhone;

    public static BsUserDetails buildUserDetails(BsUser user) {
        List<GrantedAuthority> authorities = user.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
        return new BsUserDetails(user.getId(), user.getEmail(),
            user.getPassword(), user.getFullName(), user.isEnabled(),
            authorities, user.getSignUpMethod(), null,
            user.isTwoFaEnabled(), user.getMbPhone());
    }

    /**
     * 유저네임으로 이메일이 사용되고 있음
     * @return 이메일
     */
    @Override
    public String getUsername() {
        return email;
    }
}
