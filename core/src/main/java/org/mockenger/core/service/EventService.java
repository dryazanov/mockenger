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

import java.util.List;
import java.util.Optional;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class EventService {

    public static final String DEFAULT_SORT_FIELD = "eventDate";

	@Value("${mockenger.frontend.audit.log.events.per.page}")
    public int itemsPerPage = 5;

    @Autowired
    private EventRepository<Event> eventRepository;


    public Event findById(final String id) {
        return eventRepository.findOne(id);
    }


    public Page<Event> findByEntityTypes(final List<String> eventClassTypes, final Integer page, final String sort) {
        final PageRequest pageable = new PageRequest(getPage(page), itemsPerPage, Sort.Direction.DESC, getSortField(sort));
        return eventRepository.findByEntityTypeIn(eventClassTypes, pageable);
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
        return Optional.ofNullable(eventRepository.findAll()).orElse(ImmutableList.of());
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
