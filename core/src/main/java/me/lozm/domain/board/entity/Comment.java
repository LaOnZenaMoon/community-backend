package me.lozm.domain.board.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.UseYn;
import me.lozm.global.code.converter.CommentTypeConverter;
import me.lozm.global.common.BaseEntity;
import me.lozm.global.common.HierarchicalEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Table(schema = "LOZM", name = "COMMENTS")
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "COMMENT_SEQ_GEN", sequenceName = "COMMENT_SEQ", initialValue = 1, allocationSize = 50)
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


    public void edit(CommentType commentType, String content, Long modifiedBy, UseYn useYn) {
        this.commentType = ObjectUtils.isEmpty(commentType) ? this.commentType : commentType;
        this.content = StringUtils.isEmpty(content) ? this.content : content;
        setModifiedBy(org.apache.commons.lang3.ObjectUtils.isEmpty(modifiedBy) ? getModifiedBy() : modifiedBy);
        setUse(org.apache.commons.lang3.ObjectUtils.isEmpty(useYn) ? getUse() : useYn);
    }

    public void remove(Long modifiedBy, UseYn useYn) {
        setModifiedBy(isEmpty(modifiedBy) ? getModifiedBy() : modifiedBy);
        setUse(isEmpty(useYn) ? getUse() : useYn);
    }

}
