package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.project.ProjectType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ydolzhenko on 17.06.15.
 */
public interface ProjectEntityRepository extends CrudRepository<Project, String> {

    List<Project> findByType(ProjectType type);
}
