package com.bumsoap.store.service.role;

import com.bumsoap.store.model.Role;
import com.bumsoap.store.repository.RoleRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.webauthn.api.CredentialRecord;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleServ implements RoleServInt{
    private final RoleRepoI roleRepo;

    @Override
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Role findByName(String name) {
        return roleRepo.findByName(name).orElseThrow(
                () -> new RuntimeException("없는 Role: " + name));
    }

    @Override
    public Role findById(Long id) {
        return roleRepo.findById(id).orElseThrow(
                () -> new RuntimeException("없는 Role ID: " + id));
    }

    @Override
    public void saveRole(Role role) {
        roleRepo.save(role);
    }

    @Override
    public Collection<Role> setUserRoles(List<String> roleNames) {
        return roleNames
                .stream()
                .map(name -> findByName("ROLE_" + name))
                .toList();
    }
}

