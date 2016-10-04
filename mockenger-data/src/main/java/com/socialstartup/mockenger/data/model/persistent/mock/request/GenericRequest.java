package com.socialstartup.mockenger.data.model.persistent.mock.request;

import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.base.AbstractPersistentEntity;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author Dmitry Ryazanov
 */
@ToString
@Document
public class GenericRequest extends AbstractPersistentEntity<String> {

	@NotNull(message = "groupId: may not be null")
	protected String groupId;

	protected RequestMethod method;

	protected Path path;

	protected Headers headers;

	protected Parameters parameters;

    protected Body body;

	private String checkSum;


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public RequestMethod getMethod() {
        return this.method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public Path getPath() {
        return Optional.ofNullable(path).orElse(new Path());
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Headers getHeaders() {
        return Optional.ofNullable(headers).orElse(new Headers());
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public Parameters getParameters() {
        return Optional.ofNullable(parameters).orElse(new Parameters());
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Body getBody() {
        return Optional.ofNullable(body).orElse(new Body());
    }

    public void setBody(Body body) {
        this.body = body;
    }

	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}
}
