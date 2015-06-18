package com.socialstartup.mockenger.core.service.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.mock.request.entity.PutEntity;
import com.socialstartup.mockenger.data.model.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component(value = "restPutService")
public class PutService extends RequestService {
    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     * @throws IOException
     */
    public PutEntity createMockRequest(String groupId, String requestBody, HttpServletRequest request) throws IOException {
        // Convert String to JsonObject and back to String to remove whitespaces
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        requestBody = objectMapper.writeValueAsString(jsonNode);
        Body body = new Body(requestBody);
        return (PutEntity) fillUpEntity(new PutEntity(body), groupId, request);
    }
}
