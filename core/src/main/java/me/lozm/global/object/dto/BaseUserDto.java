package me.lozm.global.object.dto;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.global.code.UsersType;
import org.springframework.util.ObjectUtils;

@SuperBuilder
@NoArgsConstructor
public class BaseUserDto {

    private Long createdBy;
    private Long modifiedBy;


    public Long getCreatedBy() {
        return ObjectUtils.isEmpty(createdBy) ? UsersType.SYSTEM.getCode() : createdBy;
    }

    public Long getModifiedBy() {
        return ObjectUtils.isEmpty(modifiedBy) ? UsersType.SYSTEM.getCode() : modifiedBy;
    }

}
