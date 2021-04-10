package me.lozm.global.common;

import me.lozm.global.code.UsersType;
import org.springframework.util.ObjectUtils;

public class BaseUserDto {

    private Long createdBy;
    private Long modifiedBy;


    public Long getCreatedBy() {
        return ObjectUtils.isEmpty(createdBy) ? UsersType.API_SYSTEM.getCode() : createdBy;
    }

    public Long getModifiedBy() {
        return ObjectUtils.isEmpty(modifiedBy) ? UsersType.API_SYSTEM.getCode() : modifiedBy;
    }

}
