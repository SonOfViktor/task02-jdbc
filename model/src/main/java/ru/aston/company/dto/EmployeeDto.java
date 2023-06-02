package ru.aston.company.dto;

import ru.aston.company.entity.Project;
import java.util.List;

public record EmployeeDto(
        long id,
        String firstName,
        String lastName,
        String positionName,
        // todo скорее всего нужен Set
        List<Project> projects
) {
}