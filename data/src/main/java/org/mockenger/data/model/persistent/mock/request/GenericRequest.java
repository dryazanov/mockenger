package org.mockenger.data.model.persistent.mock.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.base.AbstractPersistentEntity;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.mockenger.data.model.persistent.mock.request.part.Headers;
import org.mockenger.data.model.persistent.mock.request.part.Parameters;
import org.mockenger.data.model.persistent.mock.request.part.Path;

import javax.validation.constraints.NotNull;

import static java.util.Optional.ofNullable;

/**
 * @author Dmitry Ryazanov
 */
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GenericRequest extends AbstractPersistentEntity<String> {

	@NotNull(message = "groupId: may not be null")
	protected String groupId;

	protected RequestMethod method;

	protected Path path;

	protected Headers headers;

	protected Parameters parameters;

    protected Body body;

	@JsonIgnore
	private String checkSum;


    public String getGroupId() {
        return groupId;
    }

    public RequestMethod getMethod() {
        return this.method;
    }

    public Path getPath() {
        return ofNullable(path).orElse(new Path());
    }

    public Headers getHeaders() {
        return ofNullable(headers).orElse(new Headers());
    }

    public Parameters getParameters() {
        return ofNullable(parameters).orElse(new Parameters());
    }

    public Body getBody() {
        return ofNullable(body).orElse(new Body());
    }

	public String getCheckSum() {
		return checkSum;
	}
}
