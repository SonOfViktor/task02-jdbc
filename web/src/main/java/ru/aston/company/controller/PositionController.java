package ru.aston.company.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.aston.company.dto.PositionDto;
import ru.aston.company.entity.Position;
import ru.aston.company.service.PositionService;
import java.util.List;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @GetMapping
    public List<Position> findAll() {
        return positionService.findAll();
    }

    @GetMapping("/{id}")
    public PositionDto findById(@PathVariable long id) {
        return positionService.findById(id);
    }

    @GetMapping("/name/{name}")
    public PositionDto findByName(@PathVariable String name) {
        return positionService.findByName(name);
    }

    @PostMapping
    public Position addPosition(@RequestBody Position position) {
        return positionService.savePosition(position);
    }

    @PatchMapping("/{id}")
    public Position updatePosition(@PathVariable long id, @RequestBody Position position) {
        return positionService.updatePosition(id, position);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePosition(@PathVariable long id) {
        positionService.deletePosition(id);
    }
}