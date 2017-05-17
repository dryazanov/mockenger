package com.socialstartup.mockenger.core.service;

import com.google.common.collect.ImmutableList;
import com.socialstartup.mockenger.data.model.persistent.log.Eventable;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.repository.GroupEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Dmitry Ryazanov on 3/20/2015.
 */
@Component
public class GroupService {

    @Autowired
    private RequestService requestService;

    @Autowired
    private GroupEntityRepository groupEntityRepository;


    public Group findById(final String id) {
        return groupEntityRepository.findOne(id);
    }


    public Iterable<Group> findAll() {
        return Optional.ofNullable(groupEntityRepository.findAll()).orElse(ImmutableList.of());
    }


    public Iterable<Group> findByProjectId(final String projectId) {
        return Optional.ofNullable(groupEntityRepository.findByProjectId(projectId)).orElse(ImmutableList.of());
    }


    @Eventable
    public Group save(final Group entity) {
        return groupEntityRepository.save(entity);
    }


    @Eventable
    public void remove(final Group group) {
        requestService.findByGroupId(group.getId()).forEach(requestService::remove);
        groupEntityRepository.delete(group);
    }


    public static Group.GroupBuilder cloneGroup(final Group group) {
        return Group.builder()
                .id(group.getId())
                .projectId(group.getProjectId())
                .name(group.getName())
                .recording(group.isRecording())
                .forwarding(group.isForwarding())
                .forwardTo(group.getForwardTo());
    }
}
