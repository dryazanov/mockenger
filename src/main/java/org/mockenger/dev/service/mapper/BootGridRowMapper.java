package org.mockenger.dev.service.mapper;

import org.mockenger.dev.model.dto.BootGridRowDTO;
import org.mockenger.dev.model.mocks.request.IRequestEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x079089 on 6/10/2015.
 */
public class BootGridRowMapper {

    public List<BootGridRowDTO> map(List<IRequestEntity> requestEntities) {
        List<BootGridRowDTO> dtoList = new ArrayList<BootGridRowDTO>(requestEntities.size());

        for (IRequestEntity requestEntity : requestEntities) {
            dtoList.add(conver(requestEntity));
        }

        return dtoList;
    }

    private BootGridRowDTO conver(IRequestEntity requestEntity) {
        BootGridRowDTO dto = new BootGridRowDTO();

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
