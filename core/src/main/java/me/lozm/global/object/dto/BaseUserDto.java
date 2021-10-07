package me.lozm.global.object.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.global.code.UsersType;
import org.springframework.util.ObjectUtils;

import static me.lozm.global.swagger.CommonCode.*;

@SuperBuilder
@NoArgsConstructor
public class BaseUserDto {

    @ApiModelProperty(value = CREATED_BY_DESCRIPTION, example = CREATED_BY_EXAMPLE)
    private Long createdBy;

    @ApiModelProperty(value = MODIFIED_BY_DESCRIPTION, example = MODIFIED_BY_EXAMPLE)
    private Long modifiedBy;


    public Long getCreatedBy() {
        return ObjectUtils.isEmpty(createdBy) ? UsersType.SYSTEM.getCode() : createdBy;
    }

    public Long getModifiedBy() {
        return ObjectUtils.isEmpty(modifiedBy) ? UsersType.SYSTEM.getCode() : modifiedBy;
    }

}
