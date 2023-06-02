package ru.aston.company.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.aston.company.dto.EmployeeDto;
import ru.aston.company.entity.Employee;
import ru.aston.company.service.EmployeeService;
import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public EmployeeDto findById(@PathVariable long id) {
        return employeeService.findById(id);
    }

    // todo сделать имя + фамилия
    @GetMapping("/name/{name}")
    public List<EmployeeDto> findByName(@PathVariable String name) {
        return employeeService.findByName(name);
    }

    @PostMapping
    public EmployeeDto addEmployee(@RequestBody EmployeeDto employee) {
        return employeeService.saveEmployee(employee);
    }

    @PatchMapping("/{id}")
    public EmployeeDto updateEmployee(@PathVariable long id, @RequestBody EmployeeDto employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable long id) {
        employeeService.deleteEmployee(id);
    }
}