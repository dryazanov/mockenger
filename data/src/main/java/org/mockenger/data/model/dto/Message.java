package org.mockenger.data.model.dto;

/**
 * Created by Dmitry Ryazanov 04/02/2016
 */
public class Message {

    private String message;

    public Message() { }

    public Message(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}