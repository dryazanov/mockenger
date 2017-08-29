package org.mockenger.data.model.dict;


import org.mockenger.data.model.persistent.account.Account;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.request.GetRequest;
import org.mockenger.data.model.persistent.mock.request.PostRequest;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toList;

/**
 * @author  Dmitry Ryazanov
 */
public enum EventEntityType {
    ACCOUNT("Account", Account.class),
    PROJECT("Project", Project.class),
    GROUP("Group", Group.class),
    REQUEST("Request", GenericRequest.class, AbstractRequest.class, GetRequest.class, PostRequest.class);

    private String typeName;
    private List<String> classList;


    EventEntityType(final String name, final Class ...classes) {
		this.typeName = name;
        this.classList = asList(classes)
				.stream()
				.map(c -> c.getCanonicalName())
				.collect(toList());
    }


	public static List<String> getClassNames(final String entityType) {
		for (EventEntityType type : values()) {
			if (type.name().equals(entityType.toUpperCase())) {
				return type.classList;
			}
		}

		return EMPTY_LIST;
	}

	public String getTypeName() {
		return typeName;
	}
}
