package com.socialstartup.mockenger.data.model.persistent.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialstartup.mockenger.data.model.dict.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * @author Dmitry Ryazanov
 */
@Getter
@Builder
@ToString
@Document(collection = "account")
public class Account {

    @Id
    private String id;

    @NotBlank(message = "First name: may not be null or empty")
    private String firstName;

    @NotBlank(message = "Last name: may not be null or empty")
    private String lastName;

    @NotBlank(message = "Username: may not be null or empty")
    @Indexed(name = "unique_username_1", unique = true, collection = "account")
    private String username;

    private String password;

    @NotNull(message = "Role: may not be null")
    private RoleType role;



    @JsonCreator
    public Account(@JsonProperty("id") final String id,
                   @JsonProperty("firstName") final String firstName,
                   @JsonProperty("lastName") final String lastName,
                   @JsonProperty("username") final String username,
                   @JsonProperty("password") final String password,
                   @JsonProperty("role") final RoleType role) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
