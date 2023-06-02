package ru.aston.company.dto;

import ru.aston.company.entity.Employee;
import java.util.List;

public record PositionDto(
        long id,
        String name,
        List<Employee> employees
) {
}