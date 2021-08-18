package me.lozm.domain.board.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.domain.user.entity.User;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.UseYn;
import me.lozm.global.code.converter.BoardTypeConverter;
import me.lozm.global.code.converter.ContentTypeConverter;
import me.lozm.global.object.entity.HierarchicalEntity;
import me.lozm.global.object.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(schema = "LOZM", name = "BOARD")
@SequenceGenerator(name = "BOARD_SEQ_GEN", sequenceName = "BOARD_SEQ", allocationSize = 50)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GEN")
    @Column(name = "BOARD_ID")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "commonParentId", column = @Column(name = "COMMON_PARENT_BOARD_ID")),
            @AttributeOverride(name = "parentId", column = @Column(name = "PARENT_BOARD_ID"))
    })
    private HierarchicalEntity hierarchicalBoard;

    @Column(name = "BOARD_TYPE")
    @Convert(converter = BoardTypeConverter.class)
    private BoardType boardType;

    @Column(name = "CONTENT_TYPE")
    @Convert(converter = ContentTypeConverter.class)
    private ContentType contentType;

    @Column(name = "VIEW_COUNT")
    private Long viewCount;

    @Column(name = "TITLE")
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", insertable = false, updatable = false)
    private User createdUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODIFIED_BY", insertable = false, updatable = false)
    private User modifiedUser;


    public void edit(User user,
                     UseYn useYn,
                     BoardType boardType,
                     ContentType contentType,
                     String title,
                     String content) {

        setModifiedBy(user.getId());
        this.modifiedUser = user;
        setUse(isEmpty(useYn) ? UseYn.USE : useYn);
        this.boardType = isEmpty(boardType) ? this.boardType : boardType;
        this.contentType = isEmpty(contentType) ? this.contentType : contentType;
        this.title = isEmpty(title) ? this.title : title;
        this.content = isEmpty(content) ? this.content : content;
    }

    public void remove(User user) {
        setModifiedBy(user.getId());
        this.modifiedUser = user;
        setUse(UseYn.NOT_USE);
    }

    public void addViewCount() {
        this.viewCount += 1;
    }

}
