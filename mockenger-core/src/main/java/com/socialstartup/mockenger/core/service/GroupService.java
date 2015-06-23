package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.repository.GroupEntityRepository;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Profile;
import com.socialstartup.mockenger.data.model.persistent.mock.group.ProfileType;
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


    public Profile findById(String id) {
        return groupEntityRepository.findOne(id);
    }

    public List<Profile> findByType(ProfileType type) {
        return groupEntityRepository.findByType(type);
    }

    public List<Profile> findByProjectId(String projectId) {
        return groupEntityRepository.findByProjectId(projectId);
    }

    public void save(Profile entity) {
        groupEntityRepository.save(entity);
    }

    public void remove(Profile profile) {
        requestService.findByGroupId(profile.getId()).forEach(requestService::remove);
        groupEntityRepository.delete(profile);
    }
}
