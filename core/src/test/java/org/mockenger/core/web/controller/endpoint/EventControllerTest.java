package org.mockenger.core.web.controller.endpoint;

import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.data.model.dict.EventEntityType;
import org.mockenger.data.model.persistent.log.Event;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dmitry Ryazanov
 */
public class EventControllerTest extends AbstractControllerTest {

    private static final String ENDPOINT_TEMPLATE = AbstractController.API_PATH + "/events/";


    @Before
    public void setUp() {
        super.setUp();
        deleteAllEvents();
    }


    @Test
    public void testGetEvent() throws Exception {
        final Event event = createEvent();
        final ResultActions resultActions = getEventRest(event.getId());

        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
            .andExpect(jsonPath("$.id").value(event.getId()));
    }

    @Test
    public void testGetEventList() throws Exception {
        createEvent();
        createEvent();

        final ResultActions resultActions = getEventsAllRest();

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    public void testGetEventListByEntityType() throws Exception {
        createEvent();
        createEvent();

        final ResultActions resultActions = getEventsAllRest(EventEntityType.PROJECT);

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    private ResultActions getEventsAllRest() throws Exception {
        return mockMvc.perform(get(ENDPOINT_TEMPLATE).accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ResultActions getEventsAllRest(final EventEntityType type) throws Exception {
        return mockMvc.perform(
                get(ENDPOINT_TEMPLATE)
                        .param("types", type.name())
                        .accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ResultActions getEventRest(String eventId) throws Exception {
        return mockMvc.perform(
                get(ENDPOINT_TEMPLATE + eventId)
                        .accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }
}
