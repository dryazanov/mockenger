package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.repository.ProjectEntityRepository;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
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


    public Project findById(String id) {
        return projectEntityRepository.findOne(id);
    }

    public List<Project> findByType(RequestMethod.ProjectType type) {
        return projectEntityRepository.findByType(type);
    }

    public void save(Project entity) {
        projectEntityRepository.save(entity);
    }

    public void remove(Project project) {
        groupService.findByProjectId(project.getId()).forEach(groupService::remove);
        projectEntityRepository.delete(project);
    }
}
