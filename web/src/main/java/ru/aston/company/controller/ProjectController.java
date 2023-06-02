package ru.aston.company.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.aston.company.dto.ProjectDto;
import ru.aston.company.entity.Project;
import ru.aston.company.service.ProjectService;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public List<Project> showAllProjects() {
        return projectService.findAll();
    }

    @GetMapping("/{id}")
    public ProjectDto showProjectById(@PathVariable long id) {
        return projectService.findById(id);
    }

    // todo придумать вариант получше
    @GetMapping("/name/{name}")
    public ProjectDto showProjectByName(@PathVariable(value="name") String name) {
        return projectService.findByName(name);
    }

    @PostMapping
    public Project addProject(@RequestBody Project project) {
        return projectService.saveProject(project);
    }

    @PatchMapping("/{id}")
    public Project updateProject(@PathVariable long id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable long id) {
        projectService.deleteProject(id);
    }
}