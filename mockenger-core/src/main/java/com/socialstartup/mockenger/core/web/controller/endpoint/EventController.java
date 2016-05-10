package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.EventService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.core.web.exception.ObjectNotFoundException;
import com.socialstartup.mockenger.data.model.persistent.log.AccountEvent;
import com.socialstartup.mockenger.data.model.persistent.log.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Dmitry Ryazanov on 7/3/2015.
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
    public ResponseEntity getEvent(@PathVariable String eventId) {
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
    public ResponseEntity getEventList(@RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "sort", required = false) String sort) {

        final Iterable<Event> eventList = eventService.findByEntityType(AccountEvent.class, page, sort);
        return new ResponseEntity(eventList, getResponseHeaders(), HttpStatus.OK);
    }
}
