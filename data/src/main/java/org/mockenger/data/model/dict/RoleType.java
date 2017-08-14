package org.mockenger.data.model.dict;


import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Dmitry Ryazanov
 */
public enum RoleType implements GrantedAuthority {
    USER,
    MANAGER,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
