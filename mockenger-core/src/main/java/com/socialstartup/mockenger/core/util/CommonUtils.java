package com.socialstartup.mockenger.core.util;

import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Created by x079089 on 3/22/2015.
 */
public class CommonUtils {

    public static String generateUniqueId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static String getCheckSum(RequestEntity requestEntity) {
        if (requestEntity.getBody() != null && requestEntity.getBody().getValue() != null) {
            String bodyValue = requestEntity.getBody().getValue();
            if (!StringUtils.isEmpty(bodyValue)) {
                return DigestUtils.md5DigestAsHex(bodyValue.getBytes());
            }
        }
        return CommonUtils.generateCheckSum(requestEntity);
    }


    public static String generateCheckSum(RequestEntity requestEntity) {
        StringBuilder sb = new StringBuilder();
        if (requestEntity.getPath() != null && requestEntity.getPath().getValue() != null) {
            sb.append(requestEntity.getPath().getValue());
        }
        if (requestEntity.getParameters() != null && requestEntity.getParameters().getValues() != null) {
            sb.append(requestEntity.getParameters().getValues());
        }
        byte[] bytesArray = sb.append(requestEntity.getMethod()).toString().getBytes();
        return DigestUtils.md5DigestAsHex(bytesArray);
    }
}
