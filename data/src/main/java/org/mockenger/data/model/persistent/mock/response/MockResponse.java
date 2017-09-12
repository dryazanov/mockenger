package org.mockenger.data.model.persistent.mock.response;

import org.mockenger.data.model.persistent.mock.request.part.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * @author Dmitry Ryazanov
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MockResponse {

    private int httpStatus;

    private Set<Pair> headers;

    private String body;
}
