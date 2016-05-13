package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.model.dict.EventEntityType;
import com.socialstartup.mockenger.data.model.persistent.log.Event;
import com.socialstartup.mockenger.data.repository.EventRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static com.socialstartup.mockenger.core.service.EventService.DEFAULT_SORT_FIELD;
import static com.socialstartup.mockenger.core.service.EventService.ITEMS_PER_PAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Dmitry Ryazanov on 6/28/2015.
 */
public class EventServiceTest {

    private static final String EVENT_ID = "EVENT_ID";

    @InjectMocks
    private EventService classUnderTest;

    @Mock
    private Event eventMock;

    @Mock
    private EventRepository eventRepositoryMock;


    @Before
    public void init() {
        initMocks(this);

        final List<Event> eventList = Arrays.asList(eventMock);
        final PageImpl<Event> events = new PageImpl<>(eventList);

        when(eventMock.getId()).thenReturn(EVENT_ID);
        when(eventRepositoryMock.findAll()).thenReturn(eventList);
        when(eventRepositoryMock.findAll(any(PageRequest.class))).thenReturn(events);
        when(eventRepositoryMock.findOne(eq(EVENT_ID))).thenReturn(eventMock);
        when(eventRepositoryMock.findByEntityTypeIn(any(List.class), any(PageRequest.class))).thenReturn(events);
    }

    @Test
    public void testFindById() {
        final Event event = classUnderTest.findById(EVENT_ID);

        assertNotNull(event);
        assertEquals(EVENT_ID, event.getId());

        verify(eventRepositoryMock).findOne(eq(EVENT_ID));
        verifyNoMoreInteractions(eventRepositoryMock);
    }

    @Test
    public void testFindAll() {
        final List<Event> events = (List)classUnderTest.findAll();

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(EVENT_ID, events.get(0).getId());

        verify(eventRepositoryMock).findAll();
        verifyNoMoreInteractions(eventRepositoryMock);
    }

    @Test
    public void testFindAllWithParams() {
        final Page<Event> events = classUnderTest.findAll(ITEMS_PER_PAGE, DEFAULT_SORT_FIELD);

        assertNotNull(events);
        assertEquals(1, events.getNumberOfElements());
        assertEquals(EVENT_ID, events.getContent().get(0).getId());

        verify(eventRepositoryMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(eventRepositoryMock);
    }

    @Test
    public void testFindByEntityTypes() {
        final String className = EventEntityType.getClassName(EventEntityType.PROJECT.name());
        final Page<Event> events = classUnderTest.findByEntityTypes(Arrays.asList(className), ITEMS_PER_PAGE, DEFAULT_SORT_FIELD);

        assertNotNull(events);
        assertEquals(1, events.getNumberOfElements());
        assertEquals(EVENT_ID, events.getContent().get(0).getId());

        verify(eventRepositoryMock).findByEntityTypeIn(any(List.class), any(PageRequest.class));
        verifyNoMoreInteractions(eventRepositoryMock);
    }
}
