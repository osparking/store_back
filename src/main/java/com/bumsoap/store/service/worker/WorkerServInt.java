package com.bumsoap.store.service.worker;

import com.bumsoap.store.dto.PeopleByDept;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.Worker;

import java.util.List;

public interface WorkerServInt {
    List<UserDto> findAllWorkers();
    List<String> findAllDept();

    List<PeopleByDept> employeesByDept();

    int updateDeptById(Long id, String dept);

    Worker add(Worker worker);
}
