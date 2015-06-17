package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.repository.ProjectEntityRepository;
import com.socialstartup.mockenger.data.model.mock.project.ProjectEntity;
import com.socialstartup.mockenger.data.model.mock.project.ProjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by x079089 on 3/20/2015.
 */
@Component
public class ProjectService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectEntityRepository projectEntityRepository;


    public ProjectEntity findById(String projectId) {
        return projectEntityRepository.findOne(projectId);
    }

    public List<ProjectEntity> findByType(ProjectType type) {
        return projectEntityRepository.findByType(type);
    }

    public void save(ProjectEntity entity) {
        projectEntityRepository.save(entity);
    }

    public void remove(ProjectEntity projectEntity) {
        groupService.findAllByProjectId(projectEntity.getId()).forEach(groupService::remove);
        projectEntityRepository.delete(projectEntity);
    }
}
