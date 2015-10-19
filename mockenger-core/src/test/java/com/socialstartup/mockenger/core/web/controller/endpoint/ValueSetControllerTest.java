package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.dict.TransformerType;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
public class ValueSetControllerTest extends AbstractControllerTest {
    private static final String ENDPOINT_VALUESET = "/valueset";
    private static final String PROJECT_TYPES_VALUESET = ENDPOINT_VALUESET + "/projectTypes";
    private static final String REQUEST_METHODS_VALUESET = ENDPOINT_VALUESET + "/requestMethods";
    private static final String TRANSFORMER_TYPES_VALUESET = ENDPOINT_VALUESET + "/transformerTypes";
    private static final String HEADERS_VALUESET = ENDPOINT_VALUESET + "/headers";
    protected static final String CONTENT_TYPE_JSON_UTF8 = "application/json;charset=UTF-8";


    @Before
    public void setup() {
        super.setup();
        deleteAllProjects();
    }


    @Test
    public void testGetValuesetProjectTypesOk() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get(PROJECT_TYPES_VALUESET));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.[0]").value(ProjectType.REST.name()))
                .andExpect(jsonPath("$.[1]").value(ProjectType.SOAP.name()))
                .andExpect(jsonPath("$.[2]").value(ProjectType.HTTP.name()));
    }

    @Test
    public void testGetValuesetRequestMethodsOk() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get(REQUEST_METHODS_VALUESET));
        String resultJson = resultActions.andReturn().getResponse().getContentAsString();
        List<String> list = new ObjectMapper(new JsonFactory()).readValue(resultJson, ArrayList.class);

        for (String key : RequestMethod.getValueSet().keySet()) {
            assertTrue(list.contains(key));
        }
    }

    @Test
    public void testGetValuesetRequestMethodsByProjectIdOk() throws Exception {
        Project project = getNewProject();
        project.setType(ProjectType.REST);
        project = createProject(project);

        ResultActions resultActions = this.mockMvc.perform(get(REQUEST_METHODS_VALUESET).param("projectId", project.getId()));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.[0]").value(RequestMethod.GET.name()))
                .andExpect(jsonPath("$.[1]").value(RequestMethod.POST.name()))
                .andExpect(jsonPath("$.[2]").value(RequestMethod.PUT.name()))
                .andExpect(jsonPath("$.[3]").value(RequestMethod.DELETE.name()));
    }

    @Test
    public void testGetValuesetRequestMethodsByProjectId2Ok() throws Exception {
        Project project = getNewProject();
        project.setType(ProjectType.SOAP);
        project = createProject(project);

        ResultActions resultActions = this.mockMvc.perform(get(REQUEST_METHODS_VALUESET).param("projectId", project.getId()));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.[0]").value(RequestMethod.POST.name()));
    }

    @Test
    public void testGetValuesetTransformerTypesOk() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get(TRANSFORMER_TYPES_VALUESET));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.[0]").value(TransformerType.KEY_VALUE.name()))
                .andExpect(jsonPath("$.[1]").value(TransformerType.REGEXP.name()))
                .andExpect(jsonPath("$.[2]").value(TransformerType.XPATH.name()));
    }

    @Test
    public void testGetValuesetHeadersOk() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get(HEADERS_VALUESET));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(52)));
    }

    @Test
    public void testGetValuesetWithWrongProjectId() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get(REQUEST_METHODS_VALUESET).param("projectId", "wrong"));
        resultActions.andExpect(status().isNotFound()).andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
    }
}
