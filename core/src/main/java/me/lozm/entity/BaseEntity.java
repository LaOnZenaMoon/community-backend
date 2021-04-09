package me.lozm.entity;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(name = "CREATED_DATETIME", updatable = false)
    private LocalDateTime createdDateTime;

    @Column(name = "MODIFIED_DATETIME")
    private LocalDateTime modifiedDateTime;

    @Column(name = "CREATED_BY", updatable = false, nullable = false)
    private Long createdBy = -1L;

    @Column(name = "MODIFY_BY")
    private Long modifiedBy = -1L;

    @Column(name = "FLAG")
    private int flag = 1;

    public void setBaseEntity(Long createdBy, Long modifiedBy, int flag) {
        this.createdDateTime = LocalDateTime.now();
        this.createdBy = ObjectUtils.isEmpty(createdBy) ? -1L : createdBy;
        this.modifiedDateTime = LocalDateTime.now();
        this.modifiedBy = ObjectUtils.isEmpty(modifiedBy) ? -1L : modifiedBy;
        if(!StringUtils.isEmpty(flag)) this.flag = flag;
    }

}
