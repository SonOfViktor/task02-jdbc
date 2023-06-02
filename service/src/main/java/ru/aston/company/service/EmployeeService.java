package ru.aston.company.service;

import ru.aston.company.dto.EmployeeDto;
import ru.aston.company.entity.Employee;
import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    EmployeeDto findById(long id);

    List<EmployeeDto> findByName(String name);

    EmployeeDto saveEmployee(EmployeeDto employee);

    EmployeeDto updateEmployee(long id, EmployeeDto employee);

    boolean deleteEmployee(long id);
}