package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ydolzhenko on 17.06.15.
 */
public interface ProjectEntityRepository extends CrudRepository<Project, String> {}
