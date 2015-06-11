package org.mockenger.dev.model.mocks.request.soap;

import org.mockenger.dev.model.mocks.request.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class PostEntity extends RequestEntity {
    public PostEntity() {
        setMethod(RequestMethod.POST);
    }

    @Override
    public String getCheckSum() {
        return DigestUtils.md5DigestAsHex(getBody().getValue().toString().getBytes());
    }
}
