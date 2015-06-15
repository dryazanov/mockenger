package com.socialstartup.mockenger.model.mock.request.entity;

import com.socialstartup.mockenger.model.RequestType;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class PatchEntity extends BodilessEntity {
    public PatchEntity() {
        setMethod(RequestType.PATCH);
    }
}
