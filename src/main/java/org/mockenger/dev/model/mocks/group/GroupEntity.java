package org.mockenger.dev.model.mocks.group;

import org.mockenger.dev.model.mocks.MockRequestType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class GroupEntity {

    @Id
    private String id;

    private String name;

    private MockRequestType type;

    private boolean recordingStarted;

    public GroupEntity() {}

    public GroupEntity(String name, MockRequestType type, boolean recordingStarted) {
        this.name = name;
        this.type = type;
        this.recordingStarted = recordingStarted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MockRequestType getType() {
        return type;
    }

    public void setType(MockRequestType type) {
        this.type = type;
    }

    public boolean isRecordingStarted() {
        return recordingStarted;
    }

    public void setRecordingStarted(boolean recordingStarted) {
        this.recordingStarted = recordingStarted;
    }
}
