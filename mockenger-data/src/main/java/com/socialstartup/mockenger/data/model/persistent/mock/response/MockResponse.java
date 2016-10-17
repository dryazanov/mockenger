package com.socialstartup.mockenger.data.model.persistent.mock.response;

import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * @author Dmitry Ryazanov
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class MockResponse {

    private int httpStatus;

    private Set<Pair> headers;

    private String body;


    public MockResponse() {
        // default constructor
    }
}
