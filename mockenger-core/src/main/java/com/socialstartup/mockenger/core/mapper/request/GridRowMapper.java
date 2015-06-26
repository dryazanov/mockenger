package com.socialstartup.mockenger.core.mapper.request;

import com.socialstartup.mockenger.data.model.dto.RequestRow;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x079089 on 6/10/2015.
 */
public class GridRowMapper {

    public List<RequestRow> map(List<AbstractRequest> requestEntities) {
        List<RequestRow> dtoList = new ArrayList<RequestRow>(requestEntities.size());

        for (AbstractRequest abstractRequest : requestEntities) {
            dtoList.add(convert(abstractRequest));
        }

        return dtoList;
    }

    private RequestRow convert(AbstractRequest abstractRequest) {
        RequestRow dto = new RequestRow();

        dto.setId(abstractRequest.getId());
        dto.setMethod(abstractRequest.getMethod().toString());
        dto.setName(abstractRequest.getName());
        dto.setPath(abstractRequest.getPath().getValue());
        dto.setParameters(abstractRequest.getParameters().getValues().toString());

        String creationDate = "";
        if (abstractRequest.getCreationDate() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
            try{
                creationDate = simpleDateFormat.format(abstractRequest.getCreationDate());
            }catch (Exception ex ){
                System.out.println(ex);
            }
        }
        dto.setCreationDate(creationDate);

        return dto;
    }
}
