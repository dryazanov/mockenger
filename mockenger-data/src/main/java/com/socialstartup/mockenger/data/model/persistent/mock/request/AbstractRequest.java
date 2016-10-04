package com.socialstartup.mockenger.data.model.persistent.mock.request;

import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import com.socialstartup.mockenger.data.validator.MockResponseValidation;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Optional;

/**
 * @author Dmitry Ryazanov
 */
@ToString
@Document(collection = "request")
public class AbstractRequest extends GenericRequest {

    @NotBlank(message = "name: may not be null or empty")
    private String name;

    @Indexed(name = "unique_code_1", unique = true, collection = "request")
    private String uniqueCode;

    private Date creationDate;

    @MockResponseValidation
    private MockResponse mockResponse;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Body getBody() {
        return Optional.ofNullable(body).orElse(new Body());
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public MockResponse getMockResponse() {
        return mockResponse;
    }

    public void setMockResponse(MockResponse mockResponse) {
        this.mockResponse = mockResponse;
    }
}
