package ru.aston.company.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.aston.company.dao.PositionDao;
import ru.aston.company.entity.Position;
import java.util.List;

import static ru.aston.company.dao.constant.TableConstants.*;

@Repository
@RequiredArgsConstructor
public class PositionDaoImpl implements PositionDao {
    private static final String FIND_ALL_POSITIONS_SQL = """
            SELECT id, name FROM task02.positions;
            """;

    private static final String FIND_POSITION_BY_ID_SQL = """
            SELECT id, name FROM task02.positions
            WHERE id = ?;
            """;

    private static final String FIND_POSITION_BY_NAME_SQL = """
            SELECT id, name FROM task02.positions
            WHERE name = ?;
            """;

    private static final String FIND_POSITION_BY_EMPLOYEE_ID_SQL = """
            SELECT positions.id, name FROM task02.positions
            JOIN task02.employees ON positions.id = employees.position_id
            WHERE employees.id = ?
            """;

    private static final String UPDATE_POSITION_BY_ID_SQL = """
            UPDATE task02.positions
            SET name = :name
            WHERE id = :id
            """;

    private static final String DELETE_POSITION_BY_ID_SQL = """
            DELETE FROM task02.positions
            WHERE id = ?
            """;
    public static final String DEFAULT_POSITION = "Бенч";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Position> findAll() {
        return jdbcTemplate.query(FIND_ALL_POSITIONS_SQL, new BeanPropertyRowMapper<>(Position.class));
    }

    @Override
    public List<Position> findById(long id) {
        return jdbcTemplate.query(FIND_POSITION_BY_ID_SQL, new BeanPropertyRowMapper<>(Position.class), id);
    }

    @Override
    public List<Position> findByName(String name) {
        return jdbcTemplate.query(FIND_POSITION_BY_NAME_SQL, new BeanPropertyRowMapper<>(Position.class), name);
    }

    @Override
    public Position findByEmployeeId(long employeeId) {
        return jdbcTemplate.query(FIND_POSITION_BY_NAME_SQL, new BeanPropertyRowMapper<>(Position.class), employeeId)
                .stream()
                .findFirst()
                .orElseGet(() -> new Position(1, DEFAULT_POSITION));
    }

    @Override
    public Position save(Position position) {
        int positionId = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName(SCHEMA)
                .withTableName(POSITIONS)
                .usingColumns(NAME)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKey(new BeanPropertySqlParameterSource(position)).intValue();

        return new Position(positionId, position.getName());
    }

    @Override
    public int update(Position position) {
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        var beanParameterSource = new BeanPropertySqlParameterSource(position);

        return namedParameterJdbcTemplate.update(UPDATE_POSITION_BY_ID_SQL, beanParameterSource);
    }

    @Override
    public boolean deleteById(long id) {

        return jdbcTemplate.update(DELETE_POSITION_BY_ID_SQL, id) > 0;
    }
}