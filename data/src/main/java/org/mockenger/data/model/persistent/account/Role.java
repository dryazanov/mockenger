package org.mockenger.data.model.persistent.account;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Dmitry Ryazanov on 15-Sep-15.
 */
public class Role implements GrantedAuthority {

    private String role;


    public Role() {}

    public Role(String name) {
        this.role = name;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }


}