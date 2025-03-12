package com.bumsoap.store.security.user;

import com.bumsoap.store.model.BsUser;
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
    private String password;
    private boolean usable;

    private Collection<GrantedAuthority> authorities;

    public static BsUserDetails buildUserDetails(BsUser user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new BsUserDetails(user.getId(), user.getEmail(),
                user.getPassword(), user.isUsable(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
