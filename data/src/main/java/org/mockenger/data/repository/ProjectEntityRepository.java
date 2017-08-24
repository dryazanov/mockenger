package org.mockenger.data.repository;

import org.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Dmitry Ryazanov
 */
public interface ProjectEntityRepository extends CrudRepository<Project, String> {

	Project findByCode(String projectCode);
}
