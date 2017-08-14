package com.socialstartup.mockenger.data.model.persistent.mock.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Dmitry Ryazanov on 3/12/2015.
 */
@Builder
@Getter
@Document(collection = "group")
public class Group {

    @Id
    private String id;

    private String projectId;

    @NotBlank(message = "name: may not be null or empty")
    private String name;

    private boolean recording;

    private boolean forwarding;

    private String forwardTo;


    @JsonCreator
    public Group(@JsonProperty("id") final String id,
                 @JsonProperty("projectId") final String projectId,
                 @JsonProperty("name") final String name,
                 @JsonProperty("recording") final boolean recording,
                 @JsonProperty("forwarding") final boolean forwarding,
                 @JsonProperty("forwardTo") final String forwardTo) {

        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.recording = recording;
        this.forwarding = forwarding;
        this.forwardTo = forwardTo;
    }
}
