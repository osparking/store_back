package com.bumsoap.store.data;

import com.bumsoap.store.model.Admin;
import com.bumsoap.store.model.Role;
import com.bumsoap.store.repository.RoleRepoI;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.service.AdminServ;
import com.bumsoap.store.service.role.RoleServInt;
import com.bumsoap.store.util.UserType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class InitialUserCreator implements ApplicationListener<ApplicationReadyEvent> {
    private final RoleRepoI roleRepo;
    private final RoleServInt roleServ;
    private final UserRepoI userRepo;
    private final AdminServ adminServ;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_WORKER");
        insertRolesIfNotExits(defaultRoles);
        insertAdminIfNotExists();
    }

    private void insertRolesIfNotExits(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepo.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepo::save);
    }

    @Transactional
    private void insertAdminIfNotExists() {
        final String defaultEmail = "jbpark03@gmail.com";
        if (userRepo.existsByEmail(defaultEmail)) {
            return;
        }

        Admin admin = new Admin();
        Role adminRole = roleServ.findByName("ROLE_ADMIN");

        admin.setRoles(Set.of(adminRole));
        admin.setFullName("관리자");
        admin.setMbPhone("01012345678");
        admin.setEmail(defaultEmail);
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setUserType(UserType.ADMIN);
        admin.setEnabled(true);

        Admin theAdmin = adminServ.add(admin);
        System.out.println("관리자 생성 - " + defaultEmail);
    }
}
