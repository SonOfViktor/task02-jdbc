package ru.aston.company.dao;

import ru.aston.company.entity.Position;

public interface PositionDao extends BaseDao<Position> {

    Position findByEmployeeId(long employeeId);
}
