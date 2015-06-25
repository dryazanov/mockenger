package com.socialstartup.mockenger.data.model.persistent.mock.group;

import com.socialstartup.mockenger.data.model.persistent.base.AbstractPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document(collection = "group")
public class Group extends AbstractPersistentEntity<String> {

    private String projectId;

    private String name;

    private boolean recording;


    public Group() {}

    public Group(String groupId, String name,  boolean recording) {
        this.name = name;
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

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }
}
