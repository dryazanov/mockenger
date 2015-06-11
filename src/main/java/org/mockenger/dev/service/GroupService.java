package org.mockenger.dev.service;

import org.mockenger.dev.model.mocks.group.GroupEntity;
import org.mockenger.dev.repository.GroupRepository;
import org.mockenger.dev.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by x079089 on 3/20/2015.
 */
@Component
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RequestRepository requestRepository;


    public GroupEntity findById(String groupId) {
        return groupRepository.findById(groupId);
    }

    public void save(GroupEntity entity) {
        groupRepository.save(entity);
    }

    public void remove(String groupId) {
        GroupEntity entity = groupRepository.findById(groupId);
        if (entity != null) {
            requestRepository.removeAllByGroupId(groupId);
            groupRepository.remove(entity);
        }
    }
}
