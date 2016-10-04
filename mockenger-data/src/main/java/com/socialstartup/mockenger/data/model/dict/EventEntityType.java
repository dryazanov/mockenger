package com.socialstartup.mockenger.data.model.dict;


import com.socialstartup.mockenger.data.model.persistent.account.Account;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;

/**
 * @author  Dmitry Ryazanov
 */
public enum EventEntityType {
    ACCOUNT(Account.class),
    PROJECT(Project.class),
    GROUP(Group.class),
    REQUEST(AbstractRequest.class),
    GENERIC_REQUEST(GenericRequest.class);

    private String clazz;


    EventEntityType(final Class clazz) {
        this.clazz = clazz.getCanonicalName();
    }

    public static String getClassName(final String entityType) {
        for (EventEntityType type : EventEntityType.values()) {
            if (type.name().equals(entityType.toUpperCase())) {
                return type.clazz;
            }
        }

        return null;
    }
}
