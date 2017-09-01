package org.mockenger.core.service;

import com.google.common.collect.ImmutableList;
import org.mockenger.data.model.persistent.log.Event;
import org.mockenger.data.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class EventService {

    public static final String DEFAULT_SORT_FIELD = "eventDate";

	@Value("${frontend.audit.log.events.per.page}")
    public int itemsPerPage = 5;

    @Autowired
    private EventRepository<Event> eventRepository;


    public Event findById(final String id) {
        return eventRepository.findOne(id);
    }


	public Page<Event> findByEntityTypesAndEventDate(final List<String> eventClassTypes, final Date startDate,
													 final Date endDate, final Integer page, final String sort) {

		final PageRequest pageable = new PageRequest(getPage(page), itemsPerPage, DESC, getSortField(sort));

		return eventRepository.findByEntityTypeAndEventDate(eventClassTypes, startDate, endDate, pageable);
	}


    public Page<Event> findAll(final Integer page, final String sort) {
        return eventRepository.findAll(new PageRequest(getPage(page), itemsPerPage, Sort.Direction.ASC, getSortField(sort)));
    }


    private String getSortField(final String sort) {
        if (StringUtils.isEmpty(sort) || !sort.equals(DEFAULT_SORT_FIELD)) {
            return DEFAULT_SORT_FIELD;
        }
        return sort;
    }


    private Integer getPage(final Integer page) {
        if (page == null || Integer.compare(page, 0) == -1) {
            return 0;
        }
        return page;
    }


    public Iterable<Event> findAll() {
        return ofNullable(eventRepository.findAll()).orElse(ImmutableList.of());
    }


    public Event save(final Event entity) {
        return eventRepository.save(entity);
    }


    public void remove(final Event entity) {
        eventRepository.delete(entity);
    }

	public void removeAll() {
		eventRepository.deleteAll();
	}
}
