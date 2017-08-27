package org.mockenger.data.model.persistent.mock.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
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

/**
 * @author Dmitry Ryazanov
 */
@Getter
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
}


