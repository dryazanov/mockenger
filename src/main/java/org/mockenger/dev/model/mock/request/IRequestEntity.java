package org.mockenger.dev.model.mock.request;

import org.mockenger.dev.model.mock.request.part.Body;
import org.mockenger.dev.model.mock.request.part.Headers;
import org.mockenger.dev.model.mock.request.part.Parameters;
import org.mockenger.dev.model.mock.request.part.Path;
import org.mockenger.dev.model.mock.response.MockResponse;

import java.util.Date;

/**
 * Created by x079089 on 3/29/2015.
 */
public interface IRequestEntity extends IMockRequest {

    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    Date getCreationDate();

    void setCreationDate(Date creationDate);

    Path getPath();

    void setPath(Path path);

    Headers getHeaders();

    void setHeaders(Headers headers);

    Parameters getParameters();

    void setParameters(Parameters parameters);

    Body getBody();

    void setBody(Body body);

    String getCheckSum();

    void setCheckSum(String checkSum);

    MockResponse getResponse();

    void setResponse(MockResponse response);

    /*List<ITransformer> getTransformers();

    void setTransformers(List<ITransformer> transformers);*/

}
