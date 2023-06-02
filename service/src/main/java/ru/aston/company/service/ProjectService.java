package ru.aston.company.service;

import ru.aston.company.dto.ProjectDto;
import ru.aston.company.entity.Project;
import java.util.List;

public interface ProjectService {
    List<Project> findAll();

    ProjectDto findById(long id);

    ProjectDto findByName(String name);

    Project saveProject(Project project);

    Project updateProject(long id, Project project);

    boolean deleteProject(long id);
}