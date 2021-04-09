package me.lozm.global.common;

import lombok.Getter;
import me.lozm.global.code.UsersType;
import org.springframework.util.ObjectUtils;

@Getter
public class BaseUserDto {

    private Long createdBy;
    private Long modifiedBy;


    public void setCreatedBy(Long id) {
        this.createdBy = ObjectUtils.isEmpty(id) ? UsersType.API_SYSTEM.getCode() : id;
    }

    public void setModifiedBy(Long id) {
        this.modifiedBy = ObjectUtils.isEmpty(id) ? UsersType.API_SYSTEM.getCode() : id;
    }

}
