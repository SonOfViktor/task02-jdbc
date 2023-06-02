package ru.aston.company.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.aston.company.dao.EmployeeDao;
import ru.aston.company.dao.PositionDao;
import ru.aston.company.dao.ProjectDao;
import ru.aston.company.dto.EmployeeDto;
import ru.aston.company.entity.Employee;
import ru.aston.company.entity.Position;
import ru.aston.company.entity.Project;
import ru.aston.company.service.EmployeeService;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao employeeDao;
    private final PositionDao positionDao;
    private final ProjectDao projectDao;

    @Override
    public List<Employee> findAll() {
        return employeeDao.findAll();
    }

    @Override
    public EmployeeDto findById(long id) {
        return employeeDao.findById(id)
                .stream()
                .findFirst()
                .map(this::mapEmployeeToEmployeeDto)
                .orElseThrow(() -> new NoSuchElementException("Employee with id " + id + "doesn't exist"));
    }

    @Override
    public List<EmployeeDto> findByName(String name) {
        List<EmployeeDto> employees = employeeDao.findByName(name)
                .stream()
                .map(this::mapEmployeeToEmployeeDto)
                .toList();


        if (employees.isEmpty()) {
            throw new NoSuchElementException("There is no employee with specified name" + name);
        }

        return employees;
    }

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employee) {
        Employee savedEmployee = employeeDao.saveWithPosition(employee);
        employeeDao.addProjectsToEmployee(savedEmployee.getId(), employee.projects());

        return new EmployeeDto(savedEmployee.getId(),
                savedEmployee.getFirstName(),
                savedEmployee.getFirstName(),
                employee.positionName(),
                employee.projects());
    }

    // todo доделать
    @Override
    public EmployeeDto updateEmployee(long id, EmployeeDto employee) {
        return null;
    }

    @Override
    public boolean deleteEmployee(long id) {
        return employeeDao.deleteById(id);
    }

    // todo возможно тут не работает transaction
    // todo вынести в маппер
    private EmployeeDto mapEmployeeToEmployeeDto(Employee employee) {
        Position position = positionDao.findByEmployeeId(employee.getId());
        List<Project> projects = projectDao.findAllByEmployeeId(employee.getId());

        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                position.getName(),
                projects
        );
    }
}