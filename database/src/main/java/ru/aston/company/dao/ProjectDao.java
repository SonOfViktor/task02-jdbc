package ru.aston.company.dao;

import ru.aston.company.entity.Project;
import java.util.List;

public interface ProjectDao extends BaseDao<Project> {

    List<Project> findAllByEmployeeId(long id);
}
