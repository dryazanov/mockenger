package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.repository.GroupEntityRepository;
import com.socialstartup.mockenger.data.model.mock.group.GroupEntity;
import com.socialstartup.mockenger.data.model.mock.group.GroupType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by x079089 on 3/20/2015.
 */
@Component
public class GroupService {

    @Autowired
    private RequestService requestService;

    @Autowired
    private GroupEntityRepository groupEntityRepository;


    public GroupEntity findById(String groupId) {
        return groupEntityRepository.findOne(groupId);
    }

    public List<GroupEntity> findByType(GroupType type) {
        return groupEntityRepository.findByType(type);
    }

    public List<GroupEntity> findAllByProjectId(String projectId) {
        return groupEntityRepository.findByProjectId(projectId);
    }

    public void save(GroupEntity entity) {
        groupEntityRepository.save(entity);
    }

    public void remove(GroupEntity groupEntity) {
        requestService.findAllByGroupId(groupEntity.getId()).forEach(requestService::remove);
        groupEntityRepository.delete(groupEntity);
    }
}
