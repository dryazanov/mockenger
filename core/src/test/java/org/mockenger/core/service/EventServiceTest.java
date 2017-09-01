package org.mockenger.core.service;

import org.junit.Before;
import org.junit.Test;
import org.mockenger.data.model.dict.EventEntityType;
import org.mockenger.data.model.persistent.log.Event;
import org.mockenger.data.repository.EventRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockenger.core.service.EventService.DEFAULT_SORT_FIELD;
import static org.mockenger.data.model.dict.EventEntityType.PROJECT;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dmitry Ryazanov
 */
public class EventServiceTest {

    private static final String EVENT_ID = "EVENT_ID";
	private static final Integer ITEMS_PER_PAGE = 10;

	@InjectMocks
    private EventService classUnderTest;

    @Mock
    private Event eventMock;

    @Mock
    private EventRepository eventRepositoryMock;


    @Before
    public void init() {
        initMocks(this);

        final List<Event> eventList = singletonList(eventMock);
        final PageImpl<Event> events = new PageImpl<>(eventList);

        when(eventMock.getId()).thenReturn(EVENT_ID);
        when(eventRepositoryMock.findAll()).thenReturn(eventList);
        when(eventRepositoryMock.findAll(any(PageRequest.class))).thenReturn(events);
        when(eventRepositoryMock.findOne(eq(EVENT_ID))).thenReturn(eventMock);
        when(eventRepositoryMock.findByEntityTypeAndEventDate(any(List.class), any(Date.class), any(Date.class), any(PageRequest.class))).thenReturn(events);
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
        final Page<Event> events = classUnderTest.findAll(1, DEFAULT_SORT_FIELD);

        assertNotNull(events);
        assertEquals(1, events.getNumberOfElements());
        assertEquals(EVENT_ID, events.getContent().get(0).getId());

        verify(eventRepositoryMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(eventRepositoryMock);
    }

    @Test
    public void testFindByEntityTypes() {
        final List<String> className = EventEntityType.getClassNames(PROJECT.name());
        final Page<Event> events = classUnderTest.findByEntityTypesAndEventDate(className, getDate(0L),
				getDate(4099680000000L), 1, DEFAULT_SORT_FIELD);

        assertNotNull(events);
        assertEquals(1, events.getNumberOfElements());
        assertEquals(EVENT_ID, events.getContent().get(0).getId());

        verify(eventRepositoryMock).findByEntityTypeAndEventDate(any(List.class), any(Date.class), any(Date.class), any(PageRequest.class));
        verifyNoMoreInteractions(eventRepositoryMock);
    }

	private Date getDate(final Long initDate) {
		final Calendar date = Calendar.getInstance();

		date.setTimeInMillis(ofNullable(initDate).orElse(0L));

		return date.getTime();
	}
}
