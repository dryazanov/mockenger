package com.socialstartup.mockenger.core.service.mapper.request;

import com.socialstartup.mockenger.data.model.dto.RequestRowDTO;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x079089 on 6/10/2015.
 */
public class GridRowMapper {

    public List<RequestRowDTO> map(List<RequestEntity> requestEntities) {
        List<RequestRowDTO> dtoList = new ArrayList<RequestRowDTO>(requestEntities.size());

        for (RequestEntity requestEntity : requestEntities) {
            dtoList.add(convert(requestEntity));
        }

        return dtoList;
    }

    private RequestRowDTO convert(RequestEntity requestEntity) {
        RequestRowDTO dto = new RequestRowDTO();

        dto.setId(requestEntity.getId());
        dto.setMethod(requestEntity.getMethod().toString());
        dto.setName(requestEntity.getName());
        dto.setPath(requestEntity.getPath().getValue());
        dto.setParameters(requestEntity.getParameters().getValues().toString());

        String creationDate = "";
        if (requestEntity.getCreationDate() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
            try{
                creationDate = simpleDateFormat.format(requestEntity.getCreationDate());
            }catch (Exception ex ){
                System.out.println(ex);
            }
        }
        dto.setCreationDate(creationDate);

        return dto;
    }
}
