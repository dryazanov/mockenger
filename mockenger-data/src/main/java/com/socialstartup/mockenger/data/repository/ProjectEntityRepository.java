package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Dmitry Ryazanov
 */
public interface ProjectEntityRepository extends CrudRepository<Project, String> {}
