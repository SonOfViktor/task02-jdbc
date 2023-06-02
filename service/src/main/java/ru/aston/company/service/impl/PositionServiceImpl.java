package ru.aston.company.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.aston.company.dao.EmployeeDao;
import ru.aston.company.dao.PositionDao;
import ru.aston.company.dto.PositionDto;
import ru.aston.company.entity.Employee;
import ru.aston.company.entity.Position;
import ru.aston.company.service.PositionService;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionDao positionDao;
    private final EmployeeDao employeeDao;

    @Override
    public List<Position> findAll() {
        return positionDao.findAll();
    }

    @Override
    public PositionDto findById(long id) {
        return positionDao.findById(id)
                .stream()
                .findFirst()
                .map(this::mapPositionToPositionDto)
                .orElseThrow(() -> new NoSuchElementException("Position with id " + id + "doesn't exist"));
    }

    //todo repeat code
    @Override
    public PositionDto findByName(String name) {
        return positionDao.findByName(name)
                .stream()
                .findFirst()
                .map(this::mapPositionToPositionDto)
                .orElseThrow(() -> new NoSuchElementException("Position with name " + name + "doesn't exist"));
    }

    @Override
    public Position savePosition(Position position) {
        return positionDao.save(position);
    }

    @Override
    public Position updatePosition(long id, Position newPosition) {
        Position updatedPosition = positionDao.findById(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Update failed. Position with id " + id + "doesn't exist"));

        fillUpdatedPositionWithNewValues(updatedPosition, newPosition);
        positionDao.update(updatedPosition);

        return updatedPosition;
    }

    @Override
    public boolean deletePosition(long id) {
        return positionDao.deleteById(id);
    }

    private PositionDto mapPositionToPositionDto(Position position) {
        List<Employee> employees = employeeDao.findByPositionId(position.getId());

        return new PositionDto(position.getId(), position.getName(), employees);
    }

    private void fillUpdatedPositionWithNewValues(Position updatedPosition, Position newPosition) {
        if (newPosition.getName() != null) {
            updatedPosition.setName(newPosition.getName());
        }
    }
}
