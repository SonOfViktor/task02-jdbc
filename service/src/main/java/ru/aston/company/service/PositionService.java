package ru.aston.company.service;

import ru.aston.company.dto.PositionDto;
import ru.aston.company.entity.Position;
import java.util.List;

public interface PositionService {
    List<Position> findAll();

    PositionDto findById(long id);

    PositionDto findByName(String name);

    Position savePosition(Position position);

    Position updatePosition(long id, Position position);

    boolean deletePosition(long id);
}
