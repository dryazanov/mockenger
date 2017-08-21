package org.mockenger.data.repository;

import org.mockenger.data.model.persistent.mock.group.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Dmitry Ryazanov
 */
public interface GroupEntityRepository extends CrudRepository<Group, String> {

    Group findByCode(String groupCode);

    List<Group> findByProjectId(String projectId);
}
