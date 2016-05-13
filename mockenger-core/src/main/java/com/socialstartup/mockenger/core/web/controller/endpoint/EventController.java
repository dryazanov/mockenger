package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.EventService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.core.web.exception.ObjectNotFoundException;
import com.socialstartup.mockenger.data.model.dict.EventEntityType;
import com.socialstartup.mockenger.data.model.persistent.log.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Dmitry Ryazanov
 */
@Controller
public class EventController extends AbstractController {

    @Autowired
    private EventService eventService;

    /**
     * Gets one event by provided ID
     *
     * @param eventId the event id
     * @return event
     */
    @ResponseBody
    @RequestMapping(value = EVENT_ID_ENDPOINT, method = GET)
    public ResponseEntity getEvent(@PathVariable final String eventId) {
        final Event event = Optional.ofNullable(eventService.findById(eventId))
                .orElseThrow(() -> new ObjectNotFoundException("Event", eventId));

        return new ResponseEntity(event, getResponseHeaders(), HttpStatus.OK);
    }


    /**
     * Gets all the mock-request by provided group ID
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = EVENTS_ENDPOINT, method = GET)
    public ResponseEntity getEventList(@RequestParam(value = "types", required = false) final List<String> types,
                                       @RequestParam(value = "page", required = false) final Integer page,
                                       @RequestParam(value = "sort", required = false) final String sort) {

        if (types != null) {
            final List<String> typeList = types.stream()
                    .map(type -> EventEntityType.getClassName(type))
                    .filter(type -> !StringUtils.isEmpty(type))
                    .collect(Collectors.toList());

            return getResponse(eventService.findByEntityTypes(typeList, page, sort));
        }

        return getResponse(eventService.findAll(page, sort));
    }

    private ResponseEntity getResponse(final Iterable<Event> events) {
        return new ResponseEntity(events, getResponseHeaders(), HttpStatus.OK);
    }
}
