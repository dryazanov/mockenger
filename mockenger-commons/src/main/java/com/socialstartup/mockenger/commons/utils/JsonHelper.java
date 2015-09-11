package com.socialstartup.mockenger.commons.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Dmitry Ryazanov on 09-Sep-15.
 */
public class JsonHelper {

    /**
     * Convert String to JsonObject and back to String to remove whitespaces
     *
     * @param requestBody
     * @return
     * @throws IOException
     */
    public static String removeWhitespaces(String requestBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        return objectMapper.writeValueAsString(jsonNode);
    }
}
