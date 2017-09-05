package org.mockenger.core.service;

import com.google.common.collect.ImmutableList;
import org.mockenger.core.web.exception.NotUniqueValueException;
import org.mockenger.data.model.persistent.log.Eventable;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.repository.GroupEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;
import static org.mockenger.core.util.CommonUtils.cleanUpObject;

/**
 * @author Dmitry Ryazanov
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


	public Group findByCode(final String code) {
		return groupEntityRepository.findByCode(code);
	}


    public Iterable<Group> findAll() {
        return ofNullable(groupEntityRepository.findAll())
				.orElse(ImmutableList.of());
    }


    public Iterable<Group> findByProjectId(final String projectId) {
        return ofNullable(groupEntityRepository.findByProjectId(projectId))
				.orElse(ImmutableList.of());
    }


    @Eventable
    public Group save(final Group entity) {
		try {
        	return groupEntityRepository.save(entity);
		} catch (DuplicateKeyException ex) {
			throw new NotUniqueValueException(String.format("Group with the code '%s' already exists", entity.getCode()));
		}
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
				.code(group.getCode())
                .name(group.getName())
				.latency(cleanUpObject(group.getLatency()))
                .recording(group.isRecording())
                .forwarding(group.isForwarding())
                .forwardTo(group.getForwardTo());
    }
}
