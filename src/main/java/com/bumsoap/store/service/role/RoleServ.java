package com.bumsoap.store.service.role;

import com.bumsoap.store.model.Role;

import java.util.Collection;
import java.util.List;

public class RoleServ implements RoleServInt{
    @Override
    public List<Role> getRoles() {
        return List.of();
    }

    @Override
    public Role findByName(String name) {
        return null;
    }

    @Override
    public Role findById(Long id) {
        return null;
    }

    @Override
    public void saveRole(Role role) {

    }

    @Override
    public Collection<Role> setUserRoles(List<String> roleNames) {
        return List.of();
    }
}

