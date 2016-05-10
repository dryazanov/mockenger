package com.socialstartup.mockenger.core.service;

import com.google.common.collect.ImmutableList;
import com.socialstartup.mockenger.core.web.exception.NotUniqueValueException;
import com.socialstartup.mockenger.data.model.persistent.log.Eventable;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.repository.ProjectEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Dmitry Ryazanov on 3/20/2015.
 */
@Component
public class ProjectService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectEntityRepository projectEntityRepository;


    public Iterable<Project> findAll() {
        return Optional.ofNullable(projectEntityRepository.findAll()).orElse(ImmutableList.of());
    }


    public Project findById(final String id) {
        return projectEntityRepository.findOne(id);
    }


    @Eventable
    public Project save(final Project entity) {
        try {
            return projectEntityRepository.save(entity);
        } catch (DuplicateKeyException ex) {
            throw new NotUniqueValueException("Project with the code '" + entity.getCode() + "' already exist");
        }
    }


    @Eventable
    public void remove(final Project project) {
        groupService.findByProjectId(project.getId()).forEach(groupService::remove);
        projectEntityRepository.delete(project);
    }


    public synchronized long getNextSequenceValue(final String projectId) {
        final Project project = projectEntityRepository.findOne(projectId);
        return projectEntityRepository.save(getCloneWithIncrementedSequence(project)).getSequence();
    }


    private Project getCloneWithIncrementedSequence(final Project project) {
        return ProjectService.getProjectClone(project).sequence(project.getSequence() + 1).build();
    }


    public static Project.ProjectBuilder getProjectClone(final Project project) {
        return Project.builder()
                .id(project.getId())
                .name(project.getName())
                .code(project.getCode())
                .type(project.getType())
                .sequence(project.getSequence());
    }
}
