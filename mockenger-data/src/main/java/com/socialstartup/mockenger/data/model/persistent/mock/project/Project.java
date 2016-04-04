package com.socialstartup.mockenger.data.model.persistent.mock.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by Dmitry Ryazanov on 6/15/2015.
 */
@Builder
@Getter
@Document(collection = "project")
public class Project {

    @Id
    private String id;

    @NotBlank(message = "Name: may not be null or empty")
    private String name;

    @NotBlank(message = "Code: may not be null or empty")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Code: only uppercase letters and numbers allowed")
    @Indexed(name = "code_1", unique = true, collection = "project")
    private String code;

    @NotNull(message = "Type: may not be null")
    private ProjectType type;

    @JsonIgnore
    private long sequence;


    @JsonCreator
    public Project(@JsonProperty("id") final String id,
                   @JsonProperty("name") final String name,
                   @JsonProperty("code") final String code,
                   @JsonProperty("type") final ProjectType type,
                   @JsonProperty("sequence") final long sequence) {

        this.id = id;
        this.name = name;
        this.code = code;
        this.type = type;
        this.sequence = sequence;
    }
}
