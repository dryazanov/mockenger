package com.socialstartup.mockenger.core.service.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PutRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.transformer.RegexpTransformer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
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
    public PutRequest createMockRequestFromJson(String groupId, String requestBody, HttpServletRequest request) throws IOException {
        // Convert String to JsonObject and back to String to remove whitespaces
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        requestBody = objectMapper.writeValueAsString(jsonNode);

        Body body = new Body(requestBody);
        return (PutRequest) fillUpEntity(new PutRequest(body), groupId, request);
    }

    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @param removeWhitespaces
     * @return
     * @throws TransformerException
     */
    public PutRequest createMockRequestFromXml(String groupId, String requestBody, HttpServletRequest request, boolean removeWhitespaces)
            throws TransformerException {

        if (removeWhitespaces) {
            requestBody = new RegexpTransformer(">\\s+<", "><").transform(requestBody.trim());
            if (requestBody.contains("<?xml")){
                requestBody = requestBody.substring(requestBody.indexOf("?>") + 2);
            }
        }
        Body body = new Body(requestBody);
        return (PutRequest) fillUpEntity(new PutRequest(body), groupId, request);
    }
}
