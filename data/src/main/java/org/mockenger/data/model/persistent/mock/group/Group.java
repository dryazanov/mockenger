package org.mockenger.data.model.persistent.mock.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;

/**
 * @author Dmitry Ryazanov
 */
@Builder
@Getter
@Document(collection = "group")
public class Group {

    @Id
    private String id;

    private String projectId;

	@NotBlank(message = "Code: may not be null or empty")
	@Pattern(regexp = "^[A-Z0-9]+$", message = "Code: only uppercase letters and numbers allowed")
	@Indexed(name = "code_1", unique = true, collection = "group")
	private String code;

    @NotBlank(message = "name: may not be null or empty")
    private String name;

    private boolean recording;

    private boolean forwarding;

    private String forwardTo;


    @JsonCreator
    public Group(@JsonProperty("id") final String id,
                 @JsonProperty("projectId") final String projectId,
                 @JsonProperty("code") final String code,
                 @JsonProperty("name") final String name,
                 @JsonProperty("recording") final boolean recording,
                 @JsonProperty("forwarding") final boolean forwarding,
                 @JsonProperty("forwardTo") final String forwardTo) {

        this.id = id;
        this.projectId = projectId;
        this.code = code;
        this.name = name;
        this.recording = recording;
        this.forwarding = forwarding;
        this.forwardTo = forwardTo;
    }
}
