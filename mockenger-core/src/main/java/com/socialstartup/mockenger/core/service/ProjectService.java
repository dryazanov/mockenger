package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.core.web.exception.NotUniqueValueException;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.repository.ProjectEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;

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
        return projectEntityRepository.findAll();
    }

    public Project findById(String id) {
        return projectEntityRepository.findOne(id);
    }

    public List<Project> findByType(ProjectType type) {
        return projectEntityRepository.findByType(type);
    }

    public void save(Project entity) {
        try {
            projectEntityRepository.save(entity);
        } catch (DuplicateKeyException ex) {
            throw new NotUniqueValueException(String.format("Project with the code '%s' already exist", entity.getCode()));
        }
    }

    public void remove(Project project) {
        groupService.findByProjectId(project.getId()).forEach(groupService::remove);
        projectEntityRepository.delete(project);
    }

    public synchronized long getNextSequenceValue(String projectId) {
        Project project = projectEntityRepository.findOne(projectId);
        long sequenceValue = project.getSequence();
        project.setSequence(++sequenceValue);
        projectEntityRepository.save(project);
        return sequenceValue;
    }
}
