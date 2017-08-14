package org.mockenger.data.model.dict;


/**
 * Created by Dmitry Ryazanov on 3/12/2015.
 */
public enum EventType {
    SAVE,
    REMOVE,
    SEARCH,
    UNKNOWN;

    public static EventType getType(final String value) {
        for (EventType type : EventType.values()) {
            if (type.name().equals(value.toUpperCase())) {
                return type;
            }
        }

        return EventType.UNKNOWN;
    }
}
