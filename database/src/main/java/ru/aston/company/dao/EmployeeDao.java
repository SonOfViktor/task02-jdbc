package ru.aston.company.dao;

import ru.aston.company.dto.EmployeeDto;
import ru.aston.company.entity.Employee;
import ru.aston.company.entity.Project;
import java.util.List;

public interface EmployeeDao extends BaseDao<Employee> {

    List<Employee> findByPositionId(long positionId);

    Employee saveWithPosition(EmployeeDto employee);

    void addProjectsToEmployee(long id, List<Project> projects);
}