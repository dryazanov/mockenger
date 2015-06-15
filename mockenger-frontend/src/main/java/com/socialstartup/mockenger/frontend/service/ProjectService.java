package com.socialstartup.mockenger.frontend.service;

import com.socialstartup.mockenger.frontend.repository.ProjectRepository;
import com.socialstartup.mockenger.model.mock.group.GroupEntity;
import com.socialstartup.mockenger.model.mock.project.ProjectEntity;
import com.socialstartup.mockenger.model.mock.project.ProjectType;
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
    private ProjectRepository projectRepository;


    public ProjectEntity findById(String projectId) {
        return projectRepository.findById(projectId);
    }

    public List<ProjectEntity> findByType(ProjectType type) {
        return projectRepository.findAllByType(type);
    }

    public void save(ProjectEntity entity) {
        projectRepository.save(entity);
    }

    public void remove(ProjectEntity projectEntity) {
        for (GroupEntity groupEntity : groupService.findAllByProjectId(projectEntity.getId())) {
            groupService.remove(groupEntity);
        }
        projectRepository.remove(projectEntity);
    }
}
