package com.socialstartup.mockenger.data.model.persistent.mock.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.persistent.base.AbstractPersistentEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by Dmitry Ryazanov on 6/15/2015.
 */
@Document(collection = "project")
public class Project extends AbstractPersistentEntity<String> {

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

    public Project() {}

    public Project(String name, String code, ProjectType type) {
        this.name = name;
        this.code = code;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public Long getSequence() {
        return sequence;
    }

    public ProjectType getType() {
        return type;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }
}
