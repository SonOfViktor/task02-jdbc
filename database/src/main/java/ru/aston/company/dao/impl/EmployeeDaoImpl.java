package ru.aston.company.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.aston.company.dao.EmployeeDao;
import ru.aston.company.dto.EmployeeDto;
import ru.aston.company.entity.Employee;
import ru.aston.company.entity.Project;

import java.util.List;

import static ru.aston.company.dao.constant.TableConstants.*;

@Repository
@RequiredArgsConstructor
public class EmployeeDaoImpl implements EmployeeDao {
    private static final String FIND_ALL_EMPLOYEES_SQL = """
            SELECT id, first_name, last_name FROM task02.employees;
            """;

    private static final String FIND_EMPLOYEE_BY_ID_SQL = """
            SELECT id, first_name, last_name FROM task02.employees
            WHERE id = ?;
            """;

    private static final String FIND_EMPLOYEE_BY_FIRST_NAME_SQL = """
            SELECT id, first_name, last_name FROM task02.employees
            WHERE first_name = ?;
            """;

    private static final String FIND_EMPLOYEE_BY_POSITION_ID_SQL = """
            SELECT employees.id, first_name, last_name FROM task02.positions
            JOIN task02.employees ON positions.id = employees.position_id
            WHERE positions.id = ?
            """;

    private static final String ADD_EMPLOYEE_WITH_POSITION_SQL = """
            INSERT INTO task02.employees (first_name, last_name, position_id)
            VALUES (:firstName, :lastName, (
                SELECT positions.id FROM task02.positions
                WHERE positions.name = :positionName));
            """;

    private static final String ADD_PROJECT_TO_EMPLOYEE_SQL = """
            INSERT INTO task02.employee_projects (employee_id, project_id)
            VALUES (%d, (
                SELECT projects.id FROM task02.projects
                WHERE projects.name = :name));
            """;

    private static final String UPDATE_EMPLOYEE_BY_ID_SQL = """
            UPDATE task02.employees
            SET first_name = :firstName, last_name = :lastName
            WHERE id = :id
            """;

    private static final String DELETE_EMPLOYEE_BY_ID_SQL = """
            DELETE FROM task02.employees
            WHERE id = ?;
            """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Employee> findAll() {
        return jdbcTemplate.query(FIND_ALL_EMPLOYEES_SQL, new BeanPropertyRowMapper<>(Employee.class));
    }

    @Override
    public List<Employee> findById(long id) {
        return jdbcTemplate.query(FIND_EMPLOYEE_BY_ID_SQL, new BeanPropertyRowMapper<>(Employee.class), id);
    }

    @Override
    public List<Employee> findByName(String firstName) {
        return jdbcTemplate.query(FIND_EMPLOYEE_BY_FIRST_NAME_SQL, new BeanPropertyRowMapper<>(Employee.class), firstName);
    }

    @Override
    public List<Employee> findByPositionId(long positionId) {
        return jdbcTemplate.query(FIND_EMPLOYEE_BY_POSITION_ID_SQL,
                new BeanPropertyRowMapper<>(Employee.class), positionId);
    }

    @Override
    public Employee save(Employee employee) {
        int employeeId = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName(SCHEMA)
                .withTableName(EMPLOYEES)
                .usingColumns(FIRST_NAME)
                .usingColumns(LAST_NAME)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKey(new BeanPropertySqlParameterSource(employee)).intValue();
        employee.setId(employeeId);

        return employee;
    }

    @Override
    public Employee saveWithPosition(EmployeeDto employee) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        namedParameterJdbcTemplate.update(ADD_EMPLOYEE_WITH_POSITION_SQL,
                new BeanPropertySqlParameterSource(employee),
                keyHolder);

        return new Employee(keyHolder.getKey().longValue(),
                employee.firstName(),
                employee.lastName());
    }

    @Override
    public int update(Employee employee) {
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        var beanParameterSource = new BeanPropertySqlParameterSource(employee);

        return namedParameterJdbcTemplate.update(UPDATE_EMPLOYEE_BY_ID_SQL, beanParameterSource);
    }

    @Override
    public void addProjectsToEmployee(long id, List<Project> projects) {
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        SqlParameterSource[] projectSources = projects.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(
                String.format(ADD_PROJECT_TO_EMPLOYEE_SQL, id), projectSources
        );
    }



    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update(DELETE_EMPLOYEE_BY_ID_SQL, id) > 0;
    }
}