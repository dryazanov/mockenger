package org.mockenger.core.web.controller.endpoint;

import org.mockenger.core.service.EventService;
import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.core.web.exception.ObjectNotFoundException;
import org.mockenger.data.model.dict.EventEntityType;
import org.mockenger.data.model.persistent.log.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Dmitry Ryazanov
 */
@RestController
@RequestMapping(path = API_PATH + "/events")
public class EventController extends AbstractController {

    @Autowired
    private EventService eventService;


    /**
     * Gets all the mock-request by provided group ID
     *
     * @return
     */
    @GetMapping
    public ResponseEntity getEventList(@RequestParam(value = "types", required = false) final List<String> types,
                                       @RequestParam(value = "startDate", required = false, defaultValue = "0") final Long startDate,
                                       @RequestParam(value = "endDate", required = false, defaultValue = "4099680000000") final Long endDate,
                                       @RequestParam(value = "page", required = false) final Integer page,
                                       @RequestParam(value = "sort", required = false) final String sort) {

        if (types != null) {
            final List<String> typeList = types.stream()
                    .flatMap(type -> EventEntityType.getClassNames(type).stream())
                    .filter(type -> !isEmpty(type))
                    .collect(toList());

			final Page<Event> events = eventService.findByEntityTypesAndEventDate(typeList, getDate(startDate), getDate(endDate), page, sort);

			return okResponseWithDefaultHeaders(events);
        }

        return okResponseWithDefaultHeaders(eventService.findAll(page, sort));
    }


    private Date getDate(final Long initDate) {
		final Calendar date = Calendar.getInstance();

		date.setTimeInMillis(ofNullable(initDate).orElse(0L));

		return date.getTime();
	}


	/**
	 * Gets one event by provided ID
	 *
	 * @param eventId the event id
	 * @return event
	 */
	@GetMapping("/{eventId}")
	public ResponseEntity getEvent(@PathVariable final String eventId) {
		final Event event = ofNullable(eventService.findById(eventId))
				.orElseThrow(() -> new ObjectNotFoundException("Event", eventId));

		return okResponseWithDefaultHeaders(event);
	}
}
