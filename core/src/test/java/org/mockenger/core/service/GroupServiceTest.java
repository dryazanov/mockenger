package org.mockenger.core.service;

import com.google.common.collect.ImmutableList;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.DeleteRequest;
import org.mockenger.data.model.persistent.mock.request.GetRequest;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockenger.data.repository.GroupEntityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Dmitry Ryazanov on 6/28/2015.
 */
public class GroupServiceTest {

    private static final String GROUP_ID = "GROUPID";
    private static final String PROJECT_ID = "PROJECTID";

    @InjectMocks
    private GroupService classUnderTest;

    @Mock
    private Group groupMock;

    @Mock
    private RequestService requestServiceMock;

    @Mock
    private GroupEntityRepository groupEntityRepositoryMock;

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testRemove() {
        List<AbstractRequest> abstractRequestList = ImmutableList.of(new PostRequest(), new GetRequest(), new DeleteRequest());
        when(groupMock.getId()).thenReturn(GROUP_ID);
        when(requestServiceMock.findByGroupId(eq(GROUP_ID))).thenReturn(abstractRequestList);
        doNothing().when(requestServiceMock).remove(any(AbstractRequest.class));
        doNothing().when(groupEntityRepositoryMock).delete(eq(groupMock));

        classUnderTest.remove(groupMock);

        verify(groupMock).getId();
        verify(requestServiceMock).findByGroupId(eq(GROUP_ID));
        verify(requestServiceMock, times(3)).remove(any(AbstractRequest.class));
        verify(groupEntityRepositoryMock).delete(eq(groupMock));
        verifyNoMoreInteractions(groupMock, requestServiceMock, groupEntityRepositoryMock);
    }

    @Test
    public void testGetEmptyGroupList() {
        when(groupEntityRepositoryMock.findByProjectId(eq(PROJECT_ID))).thenReturn(null);

        final Iterable<Group> groups = classUnderTest.findByProjectId(PROJECT_ID);

        assertNotNull(groups);
        assertFalse(groups.iterator().hasNext());

        verify(groupEntityRepositoryMock).findByProjectId(eq(PROJECT_ID));
        verifyNoMoreInteractions(groupEntityRepositoryMock);
    }
}
