package org.mockenger.commons.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author Dmitry Ryazanov
 */
public class JsonHelper {

    /**
     * Convert String to JsonObject and back to String to remove whitespaces
     *
     * @param requestBody
     * @return
     * @throws IOException
     */
    public static String removeWhitespaces(final String requestBody) throws IOException {
		final JsonFactory jsonFactory = new JsonFactory();
		final ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        final JsonNode jsonNode = objectMapper.readTree(requestBody);

        return objectMapper.writeValueAsString(jsonNode);
    }
}
