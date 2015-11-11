package com.socialstartup.mockenger.data.model.dto;

import com.socialstartup.mockenger.data.model.dict.RoleType;

/**
 * Created by Dmitry Ryazanov
 */
public class AccountDTO {

    private String id;

    private String firstName;

    private String lastName;

    private String username;

    private RoleType role;

    public AccountDTO(String firstName, String lastName, String username, RoleType role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
