package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.repository.GroupEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Dmitry Ryazanov on 3/20/2015.
 */
@Component
public class GroupService {

    @Autowired
    private RequestService requestService;

    @Autowired
    private GroupEntityRepository groupEntityRepository;


    public Group findById(String id) {
        return groupEntityRepository.findOne(id);
    }

    public Iterable<Group> findAll() {
        return groupEntityRepository.findAll();
    }

    public List<Group> findByProjectId(String projectId) {
        return groupEntityRepository.findByProjectId(projectId);
    }

    public void save(Group entity) {
        groupEntityRepository.save(entity);
    }

    public void remove(Group profile) {
        requestService.findByGroupId(profile.getId()).forEach(requestService::remove);
        groupEntityRepository.delete(profile);
    }
}
