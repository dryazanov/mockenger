package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.mock.project.ProjectEntity;
import com.socialstartup.mockenger.data.model.mock.project.ProjectType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ydolzhenko on 17.06.15.
 */
public interface ProjectEntityRepository extends CrudRepository<ProjectEntity, String> {

    List<ProjectEntity> findByType(ProjectType type);
}
