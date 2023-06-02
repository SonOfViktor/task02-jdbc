package ru.aston.company.dto;

import ru.aston.company.entity.Employee;
import java.util.List;

public record ProjectDto(
        long id,
        String name,
        List<Employee> employees
) {
}
