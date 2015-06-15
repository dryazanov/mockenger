package com.socialstartup.mockenger.frontend.service;

import com.socialstartup.mockenger.frontend.repository.GroupRepository;
import com.socialstartup.mockenger.model.mock.group.GroupEntity;
import com.socialstartup.mockenger.model.mock.group.GroupType;
import com.socialstartup.mockenger.model.mock.request.RequestEntity;
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
    private GroupRepository groupRepository;


    public GroupEntity findById(String groupId) {
        return groupRepository.findById(groupId);
    }

    public List<GroupEntity> findByType(GroupType type) {
        return groupRepository.findAllByType(type);
    }

    public List<GroupEntity> findAllByProjectId(String projectId) {
        return groupRepository.findAllByProjectId(projectId);
    }

    public void save(GroupEntity entity) {
        groupRepository.save(entity);
    }

    public void remove(GroupEntity groupEntity) {
        for (RequestEntity requestEntity : requestService.findAllByGroupId(groupEntity.getId())) {
            requestService.remove(requestEntity);
        }
        groupRepository.remove(groupEntity);
    }
}
