package com.socialstartup.mockenger.data.model.mock.request.entity;

import com.socialstartup.mockenger.data.model.RequestType;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class OptionsEntity extends BodilessEntity {
    public OptionsEntity() {
        setMethod(RequestType.OPTIONS);
    }
}
