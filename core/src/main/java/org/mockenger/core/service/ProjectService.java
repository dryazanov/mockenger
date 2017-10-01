package org.mockenger.core.service;

import org.mockenger.core.web.exception.NotUniqueValueException;
import org.mockenger.data.model.persistent.log.Eventable;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.repository.ProjectEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author Dmitry Ryazanov
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


    public Project findById(final String id) {
        return projectEntityRepository.findOne(id);
    }


	public Project findByCode(final String code) {
		return projectEntityRepository.findByCode(code);
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


	@Eventable
	public void removeAll() {
    	groupService.removeAll();
		projectEntityRepository.deleteAll();
	}


    public synchronized long getNextSequenceValue(final String projectId) {
        final Project project = projectEntityRepository.findOne(projectId);
        return projectEntityRepository.save(getCloneWithIncrementedSequence(project)).getSequence();
    }


    private Project getCloneWithIncrementedSequence(final Project project) {
        return cloneProject(project).sequence(project.getSequence() + 1).build();
    }


    public static Project.ProjectBuilder cloneProject(final Project project) {
        return Project.builder()
                .id(project.getId())
                .name(project.getName())
                .code(project.getCode())
                .type(project.getType())
                .sequence(project.getSequence());
    }
}
