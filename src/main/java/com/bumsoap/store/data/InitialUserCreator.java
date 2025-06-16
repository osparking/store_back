package com.bumsoap.store.data;

import com.bumsoap.store.model.Admin;
import com.bumsoap.store.model.Customer;
import com.bumsoap.store.model.Role;
import com.bumsoap.store.model.Worker;
import com.bumsoap.store.repository.RoleRepoI;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.service.AdminServ;
import com.bumsoap.store.service.CustomerServ;
import com.bumsoap.store.service.role.RoleServInt;
import com.bumsoap.store.service.worker.WorkerServInt;
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
    private final CustomerServ customerServ;
    private final WorkerServInt workerServ;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_WORKER");
        insertRolesIfNotExits(defaultRoles);
        insertAdminIfNotExists();
        insertCustomersIfNotExists();
        insertWorkersIfNotExists();
    }

    private void insertRolesIfNotExits(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepo.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepo::save);
    }

    private void insertWorkersIfNotExists() {
        Role workerRole = roleServ.findByName("ROLE_WORKER");

        for (int i = 1; i <= 10; i++) {
            String workerEmail = "worker" + i + "@email.com";
            if (userRepo.existsByEmail(workerEmail)) {
                continue;
            }
            Worker worker = new Worker();

            worker.setFullName("직원" + i);
            worker.setMbPhone("0104567880" + (i - 1));
            worker.setEmail(workerEmail);
            worker.setPassword(passwordEncoder.encode("1234"));
            worker.setUserType(UserType.WORKER);
            worker.setRoles(Set.of(workerRole));
            worker.setEnabled(true);
            worker.setDept("생산부");
            worker.setSignUpMethod("EMAIL");
            worker = workerServ.add(worker);

            System.out.println("생성된 직원 구분번호: " + i);
        }
    }

    private void insertCustomersIfNotExists() {
        Role customerRole = roleServ.findByName("ROLE_CUSTOMER");

        for (int i = 1; i <= 10; i++) {
            String defaultEmail = "customer" + i + "@email.com";
            if (userRepo.existsByEmail(defaultEmail)) {
                continue;
            }
            Customer customer = new Customer();
            customer.setFullName("고객" + i);
            customer.setMbPhone("0104567890" + (i - 1));
            customer.setEmail(defaultEmail);
            customer.setPassword(passwordEncoder.encode("1234"));
            customer.setUserType(UserType.CUSTOMER);
            customer.setRoles(Set.of(customerRole));
            customer.setEnabled(true);
            customer.setSignUpMethod("EMAIL");
            customer = customerServ.add(customer);
            System.out.println("생성된 고객 일련번호: " + i);
        }
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
        admin.setSignUpMethod("EMAIL");

        Admin theAdmin = adminServ.add(admin);
        System.out.println("관리자 생성 - " + defaultEmail);
    }
}
