package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ydolzhenko on 17.06.15.
 */
public interface GroupEntityRepository extends CrudRepository<Group, String> {

    List<Group> findByProjectId(String projectId);
}
