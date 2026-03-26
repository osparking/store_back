package com.bumsoap.store.service.employee;

import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.model.Employee;
import com.bumsoap.store.row.EmployeeNameRow;

public interface EmployeeServInt {
    Employee findById(Long empId);
    Employee save(Employee employee);

    SearchResult<EmployeeNameRow> getEmpNamesPage(
            Integer page, Integer size, String name);
}
