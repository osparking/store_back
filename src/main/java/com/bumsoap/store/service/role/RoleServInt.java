package com.bumsoap.store.service.role;

import com.bumsoap.store.model.Role;

import java.util.Collection;
import java.util.List;

public interface RoleServInt {
    List<Role> getRoles();
    Role findByName(String name);
    Role findById(Long id);
    void saveRole(Role role);
    Collection<Role> setUserRoles(List<String> roleNames);
}
