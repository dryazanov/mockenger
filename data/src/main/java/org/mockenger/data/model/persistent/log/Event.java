package org.mockenger.data.model.persistent.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mockenger.data.model.dict.EventResultType;
import org.mockenger.data.model.dict.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Dmitry Ryazanov
 */
@Getter
@Builder
@ToString
@Document(collection = "event")
public class Event<T> {

	@Id
	private String id;

	private T entity;

	private EventType eventType;

	private Date eventDate;

	private String username;

	private EventResultType resultType;


	@JsonCreator
	public Event(@JsonProperty("id") final String id,
				 @JsonProperty("entity") final T entity,
				 @JsonProperty("eventType") final EventType eventType,
				 @JsonProperty("timestamp") final Date eventDate,
				 @JsonProperty("username") final String username,
				 @JsonProperty("resultType") final EventResultType resultType) {

		this.id = id;
		this.entity = entity;
		this.eventType = eventType;
		this.eventDate = eventDate;
		this.username = username;
		this.resultType = resultType;
	}
}
