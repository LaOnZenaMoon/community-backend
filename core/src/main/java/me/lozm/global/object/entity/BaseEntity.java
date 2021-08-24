package me.lozm.global.object.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.lozm.domain.user.entity.User;
import me.lozm.global.code.UseYn;
import me.lozm.global.code.converter.UseYnConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity {

    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDateTime;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDateTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", updatable = false)
    private User createdUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODIFIED_BY")
    private User modifiedUser;

    @Column(name = "USE_YN")
    @Convert(converter = UseYnConverter.class)
    private UseYn use = UseYn.USE;

}

