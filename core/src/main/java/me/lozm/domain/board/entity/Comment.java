package me.lozm.domain.board.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.domain.user.entity.User;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.UseYn;
import me.lozm.global.code.converter.CommentTypeConverter;
import me.lozm.global.object.entity.BaseEntity;
import me.lozm.global.object.entity.HierarchicalEntity;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.*;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;


@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(schema = "LOZM", name = "COMMENTS")
@SequenceGenerator(name = "COMMENT_SEQ_GEN", sequenceName = "COMMENT_SEQ", allocationSize = 50)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GEN")
    @Column(name = "COMMENT_ID")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "commonParentId", column = @Column(name = "COMMON_PARENT_COMMENT_ID")),
            @AttributeOverride(name = "parentId", column = @Column(name = "PARENT_COMMENT_ID"))
    })
    private HierarchicalEntity hierarchicalBoard;

    @Column(name = "COMMENT_TYPE")
    @Convert(converter = CommentTypeConverter.class)
    private CommentType commentType;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", insertable = false, updatable = false)
    private User createdUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODIFIED_BY", insertable = false, updatable = false)
    private User modifiedUser;


    public void edit(User user, UseYn useYn, CommentType commentType, String content) {
        setModifiedBy(user.getId());
        this.modifiedUser = user;
        setModifiedDateTime(LocalDateTime.now());
        setUse(isEmpty(UseYn.USE) ? UseYn.USE : useYn);
        this.commentType = isEmpty(commentType) ? this.commentType : commentType;
        this.content = isEmpty(content) ? this.content : content;
    }

    public void remove(User user) {
        setModifiedBy(user.getId());
        this.modifiedUser = user;
        setModifiedDateTime(LocalDateTime.now());
        setUse(UseYn.NOT_USE);
    }

}
