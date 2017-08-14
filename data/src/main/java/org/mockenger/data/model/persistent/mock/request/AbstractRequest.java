package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.persistent.mock.response.MockResponse;
import org.mockenger.data.validator.MockResponseValidation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Dmitry Ryazanov
 */
@Getter
@Setter
@ToString
@Document(collection = "request")
public class AbstractRequest extends GenericRequest {

    @NotBlank(message = "name: may not be null or empty")
    private String name;

    @Indexed(name = "unique_code_1", unique = true, collection = "request")
    private String uniqueCode;

    private Date creationDate;

    private Date lastUpdateDate;

    @MockResponseValidation
    private MockResponse mockResponse;

	private long requestCounter;
}
