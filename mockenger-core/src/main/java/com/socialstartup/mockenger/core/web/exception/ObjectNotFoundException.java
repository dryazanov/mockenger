package com.socialstartup.mockenger.core.web.exception;

/**
 * Created by x079089 on 6/19/2015.
 */
public class ObjectNotFoundException extends RuntimeException {

    private String className;

    private String itemId;

    public ObjectNotFoundException(String className, String itemId) {
        this.className = className;
        this.itemId = itemId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
