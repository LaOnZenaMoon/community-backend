package me.lozm.object.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter @SuperBuilder
public class BaseVo {

    private LocalDateTime createdDt;
    private LocalDateTime modifiedDt;
    private Long createdBy;
    private Long modifiedBy;
    private int flag;

}
