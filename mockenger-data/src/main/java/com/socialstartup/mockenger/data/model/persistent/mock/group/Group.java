package com.socialstartup.mockenger.data.model.persistent.mock.group;

import com.socialstartup.mockenger.data.model.persistent.base.AbstractPersistentEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document(collection = "group")
public class Group extends AbstractPersistentEntity<String> {

    private String projectId;

    @NotBlank(message = "name: may not be null or empty")
    private String name;

    private boolean recording;

    private boolean forwarding;

    private String forwardTo;


    public Group() {}

    public Group(String projectId, String name, boolean recording) {
        this.projectId = projectId;
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

    public boolean isForwarding() {
        return forwarding;
    }

    public void setForwarding(boolean forwarding) {
        this.forwarding = forwarding;
    }

    public String getForwardTo() {
        return forwardTo;
    }

    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }
}
