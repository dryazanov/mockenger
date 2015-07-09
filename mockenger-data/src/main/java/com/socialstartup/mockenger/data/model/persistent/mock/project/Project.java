package com.socialstartup.mockenger.data.model.persistent.mock.project;

import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.persistent.base.AbstractPersistentEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by x079089 on 6/15/2015.
 */
@Document(collection = "project")
public class Project extends AbstractPersistentEntity<String> {

    @NotBlank(message = "name: may not be null or empty")
    private String name;

    @NotNull(message = "type: may not be null")
    private ProjectType type;

    public Project() {}

    public Project(String name, ProjectType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectType getType() {
        return type;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }
}
