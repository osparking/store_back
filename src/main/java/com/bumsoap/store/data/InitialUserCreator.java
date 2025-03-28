package com.bumsoap.store.data;

import com.bumsoap.store.model.Role;
import com.bumsoap.store.repository.RoleRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class InitialUserCreator implements ApplicationListener<ApplicationReadyEvent> {
    private final RoleRepoI roleRepo;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_WORKER");
        insertRolesIfNotExits(defaultRoles);
    }

    private void insertRolesIfNotExits(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepo.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepo::save);
    }
}
