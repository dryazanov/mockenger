package org.mockenger.data.model.persistent.log;

import org.mockenger.data.model.dict.RequestMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by dryazanov on 05/04/16.
 */
@Getter
@Builder
@ToString
public class EndpointHit {

    @Id
    private String id;

    private Date date;

    private String path;

    private RequestMethod method;
}
