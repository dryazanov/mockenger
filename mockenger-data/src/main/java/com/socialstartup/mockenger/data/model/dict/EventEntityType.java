package com.socialstartup.mockenger.data.model.dict;


import com.socialstartup.mockenger.data.model.persistent.log.AccountEvent;
import com.socialstartup.mockenger.data.model.persistent.log.GroupEvent;
import com.socialstartup.mockenger.data.model.persistent.log.ProjectEvent;
import com.socialstartup.mockenger.data.model.persistent.log.RequestEvent;

/**
 * @author  Dmitry Ryazanov
 */
public enum EventEntityType {
    ACCOUNT(AccountEvent.class),
    PROJECT(ProjectEvent.class),
    GROUP(GroupEvent.class),
    REQUEST(RequestEvent.class);

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
