package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.persistent.mock.group.Profile;
import com.socialstartup.mockenger.data.model.persistent.mock.group.ProfileType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ydolzhenko on 17.06.15.
 */
public interface GroupEntityRepository extends CrudRepository<Profile, String> {

    List<Profile> findByType(ProfileType type);

    List<Profile> findByProjectId(String projectId);
}
