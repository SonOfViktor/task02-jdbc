package ru.aston.company.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.aston.company.dao.ProjectDao;
import ru.aston.company.entity.Project;
import java.util.List;

import static ru.aston.company.dao.constant.TableConstants.*;

@Repository
@RequiredArgsConstructor
public class ProjectDaoImpl implements ProjectDao {
    private final JdbcTemplate jdbcTemplate;
    private static final String FIND_ALL_PROJECTS_SQL = """
            SELECT id, name FROM task02.projects;
            """;

    private static final String FIND_PROJECT_BY_ID_SQL = """
            SELECT id, name FROM task02.projects
            WHERE id = ?;
            """;

    private static final String FIND_PROJECT_BY_NAME_SQL = """
            SELECT id, name FROM task02.projects
            WHERE name = ?;
            """;

    private static final String FIND_PROJECT_BY_EMPLOYEE_ID = """
            SELECT pr.id, name FROM task02.projects pr
            JOIN task02.employee_projects ep ON pr.id = ep.project_id
            JOIN task02.employees em ON ep.employee_id = em.id
            WHERE em.id = ?;
            """;

    private static final String UPDATE_PROJECT_BY_ID_SQL = """
            UPDATE task02.projects
            SET name = :name
            WHERE id = :id
            """;

    private static final String DELETE_PROJECT_BY_ID_SQL = """
            DELETE FROM task02.projects
            WHERE id = ?
            """;

    @Override
    public List<Project> findAll() {
        return jdbcTemplate.query(FIND_ALL_PROJECTS_SQL, new BeanPropertyRowMapper<>(Project.class));
    }

    @Override
    public List<Project> findById(long id) {
        return jdbcTemplate.query(FIND_PROJECT_BY_ID_SQL, new BeanPropertyRowMapper<>(Project.class), id);
    }

    @Override
    public List<Project> findByName(String name) {
        return jdbcTemplate.query(FIND_PROJECT_BY_NAME_SQL, new BeanPropertyRowMapper<>(Project.class), name);
    }

    @Override
    public List<Project> findAllByEmployeeId(long employeeId) {
        return jdbcTemplate.query(FIND_PROJECT_BY_EMPLOYEE_ID, new BeanPropertyRowMapper<>(Project.class), employeeId);
    }

    // todo return project copy not project in parameters
    @Override
    public Project save(Project project) {
        int projectId = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName(SCHEMA)
                .withTableName(PROJECTS)
                .usingColumns(NAME)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKey(new BeanPropertySqlParameterSource(project)).intValue();
        project.setId(projectId);

        return project;
    }

    @Override
    public int update(Project project) {
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        var beanParameterSource = new BeanPropertySqlParameterSource(project);

        return namedParameterJdbcTemplate.update(UPDATE_PROJECT_BY_ID_SQL, beanParameterSource);
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update(DELETE_PROJECT_BY_ID_SQL, id) > 0;
    }
}
