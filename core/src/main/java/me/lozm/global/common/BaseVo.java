package me.lozm.global.common;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import me.lozm.global.code.UseYn;

import java.time.LocalDateTime;

@Getter @SuperBuilder
public class BaseVo {

    private LocalDateTime createDateTime;
    private LocalDateTime modifiedDateTime;
    private Long createdBy;
    private Long modifiedBy;
    private UseYn use;

}
