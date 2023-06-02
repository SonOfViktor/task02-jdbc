package ru.aston.company.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.aston.company.dao.EmployeeDao;
import ru.aston.company.dao.ProjectDao;
import ru.aston.company.dto.ProjectDto;
import ru.aston.company.entity.Employee;
import ru.aston.company.entity.Project;
import ru.aston.company.service.ProjectService;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDao projectDao;
    private final EmployeeDao employeeDao;
    
    @Override
    public List<Project> findAll() {
        return projectDao.findAll();
    }

    @Override
    public ProjectDto findById(long id) {
        return projectDao.findById(id)
                .stream()
                .findFirst()
                .map(this::mapProjectToProjectDto)
                .orElseThrow(() -> new NoSuchElementException("Project with id " + id + "doesn't exist"));
    }

    @Override
    public ProjectDto findByName(String name) {
        return projectDao.findByName(name)
                .stream()
                .findFirst()
                .map(this::mapProjectToProjectDto)
                .orElseThrow(() -> new NoSuchElementException("Project with name " + name + "doesn't exist"));
    }

    @Override
    public Project saveProject(Project project) {
        return projectDao.save(project);
    }

    @Override
    public Project updateProject(long id, Project newProject) {
        Project updatedProject = projectDao.findById(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Update failed. Project with id " + id + "doesn't exist"));

        fillUpdatedProjectWithNewValues(updatedProject, newProject);
        projectDao.update(updatedProject);

        return updatedProject;
    }


    @Override
    public boolean deleteProject(long id) {
        return projectDao.deleteById(id);
    }

    private ProjectDto mapProjectToProjectDto(Project project) {
        List<Employee> employees = employeeDao.findByPositionId(project.getId());

        return new ProjectDto(project.getId(), project.getName(), employees);
    }

    private void fillUpdatedProjectWithNewValues(Project updatedProject, Project newProject) {
        if (newProject.getName() != null) {
            updatedProject.setName(newProject.getName());
        }
    }
}