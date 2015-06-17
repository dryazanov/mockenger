package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.mock.group.GroupEntity;
import com.socialstartup.mockenger.data.model.mock.group.GroupType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ydolzhenko on 17.06.15.
 */
public interface GroupEntityRepository extends CrudRepository<GroupEntity, String> {

    List<GroupEntity> findByType(GroupType type);

    List<GroupEntity> findByProjectId(String projectId);

}
