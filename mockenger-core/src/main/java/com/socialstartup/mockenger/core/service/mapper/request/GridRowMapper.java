package com.socialstartup.mockenger.core.service.mapper.request;

import com.socialstartup.mockenger.data.model.dto.RequestRowDTO;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x079089 on 6/10/2015.
 */
public class GridRowMapper {

    public List<RequestRowDTO> map(List<AbstractRequest> requestEntities) {
        List<RequestRowDTO> dtoList = new ArrayList<RequestRowDTO>(requestEntities.size());

        for (AbstractRequest abstractRequest : requestEntities) {
            dtoList.add(convert(abstractRequest));
        }

        return dtoList;
    }

    private RequestRowDTO convert(AbstractRequest abstractRequest) {
        RequestRowDTO dto = new RequestRowDTO();

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
