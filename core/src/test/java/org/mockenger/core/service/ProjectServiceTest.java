package org.mockenger.core.service;

import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.repository.ProjectEntityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;

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
public class ProjectServiceTest {

    private static final String PROJECT_ID = "PROJECTID";

    @InjectMocks
    private ProjectService classUnderTest;

    @Mock
    private Project projectMock;

    @Mock
    private GroupService groupServiceMock;

    @Mock
    private ProjectEntityRepository projectEntityRepositoryMock;

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testRemove() {
        when(projectMock.getId()).thenReturn(PROJECT_ID);
        when(groupServiceMock.findByProjectId(eq(PROJECT_ID))).thenReturn(Collections.nCopies(2, Group.builder().build()));
        doNothing().when(groupServiceMock).remove(any(Group.class));
        doNothing().when(projectEntityRepositoryMock).delete(eq(projectMock));

        classUnderTest.remove(projectMock);

        verify(projectMock).getId();
        verify(groupServiceMock).findByProjectId(eq(PROJECT_ID));
        verify(groupServiceMock, times(2)).remove(any(Group.class));
        verify(projectEntityRepositoryMock).delete(eq(projectMock));
        verifyNoMoreInteractions(projectMock, groupServiceMock, projectEntityRepositoryMock);
    }
}
