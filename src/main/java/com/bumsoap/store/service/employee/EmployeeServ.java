package com.bumsoap.store.service.employee;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.Employee;
import com.bumsoap.store.repository.EmployeeRepoI;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServ implements EmployeeServInt{
    private final EmployeeRepoI employeeRepo;

    @Override
    public Employee findById(Long empId) {
        return employeeRepo.findById(empId).orElseThrow(
                () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + empId));
    }
}
