package com.socialstartup.mockenger.data.model;

/**
 * Created by ydolzhenko on 15.06.15.
 */
public enum RequestType {

    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE,
    CONNECT;

    private RequestType() {
    }
}
