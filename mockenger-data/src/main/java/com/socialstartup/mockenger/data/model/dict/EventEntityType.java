package com.socialstartup.mockenger.data.model.dict;


import com.socialstartup.mockenger.data.model.persistent.account.Account;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GetRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PostRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

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


    EventEntityType(final String name, final Class ... classes) {
		this.typeName = name;
        this.classList = asList(classes).parallelStream()
				.map(c -> c.getCanonicalName())
				.collect(Collectors.toList());
    }


	public static List<String> getClassNames(final String entityType) {
		for (EventEntityType type : EventEntityType.values()) {
			if (type.name().equals(entityType.toUpperCase())) {
				return type.classList;
			}
		}

		return Collections.EMPTY_LIST;
	}

	public String getTypeName() {
		return typeName;
	}
}
