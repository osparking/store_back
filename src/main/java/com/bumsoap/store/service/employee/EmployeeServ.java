package com.bumsoap.store.service.employee;

import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.Employee;
import com.bumsoap.store.repository.EmployeeRepoI;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.row.EmployeeNameRow;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class EmployeeServ implements EmployeeServInt{
    private final EmployeeRepoI employeeRepo;
    private final UserRepoI userRepoI;

    @Override
    public Employee findById(Long empId) {
        return employeeRepo.findById(empId).orElseThrow(
                () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + empId));
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepo.save(employee);
    }

    @Override
    public SearchResult<EmployeeNameRow> getEmpNamesPage(
            Integer page, Integer size, String name) {

        Pageable pageable = PageRequest.of(page - 1, size);
        var employeeNamePage = userRepoI.getEmployeeNames(
                UserType.WORKER.ordinal(), name, pageable);
        int totalPages = employeeNamePage.getTotalPages();
        List<Integer> pageNumbers = null;

        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        return new SearchResult<>(employeeNamePage,
                employeeNamePage.getNumber() + 1,
                size, totalPages, pageNumbers
        );
    }
}
