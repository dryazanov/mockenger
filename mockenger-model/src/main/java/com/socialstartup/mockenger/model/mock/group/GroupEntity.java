package com.socialstartup.mockenger.model.mock.group;

import com.socialstartup.mockenger.model.persistent.base.AbstractPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class GroupEntity extends AbstractPersistentEntity<String> {

    private String projectId;

    private String name;

    private GroupType type;

    private boolean recording;


    public GroupEntity() {}

    public GroupEntity(String groupId, String name, GroupType type, boolean recording) {
        this.name = name;
        this.type = type;
        this.recording = recording;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }
}
