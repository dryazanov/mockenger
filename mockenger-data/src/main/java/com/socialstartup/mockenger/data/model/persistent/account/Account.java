package com.socialstartup.mockenger.data.model.persistent.account;

import com.socialstartup.mockenger.data.model.dict.RoleType;
import com.socialstartup.mockenger.data.model.persistent.base.AbstractPersistentEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by Dmitry Ryazanov on 14-Sep-15.
 */
@Document(collection = "account")
public class Account extends AbstractPersistentEntity<String> {

    @NotBlank(message = "firstName: may not be null or empty")
    private String firstName;

    @NotBlank(message = "lastName: may not be null or empty")
    private String lastName;

    @NotBlank(message = "username: may not be null or empty")
    private String username;

    private String password;

    @NotNull(message = "role: may not be null")
    private RoleType role;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRoles(RoleType role) {
        this.role = role;
    }
}
