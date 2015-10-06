package com.socialstartup.mockenger.data.model.dto;

import com.socialstartup.mockenger.data.model.dict.RoleType;

import java.util.Set;

/**
 * Created by Dmitry Ryazanov
 */
public class AccountDTO {

    private String firstName;

    private String lastName;

    private String username;

    private Set<RoleType> roles;

    public AccountDTO(String firstName, String lastName, String username, Set<RoleType> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.roles = roles;
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

    public Set<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }
}
