package me.lozm.entity.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.entity.BaseEntity;
import me.lozm.object.vo.board.CommentVo;

import javax.persistence.*;

@Table(schema = "LOZM", name = "COMMENTS")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "COMMENT_SEQ_GEN", sequenceName = "COMMENT_SEQ", initialValue = 1, allocationSize = 50)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GEN")
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(name = "COMMENT_TYPE")
    private String commentType;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;


    public void insertComment(CommentVo commentVo, Board board) {
        this.commentType = String.valueOf(commentVo.getCommentType());
        this.content = commentVo.getContent();
        this.board = board;
        this.setBaseEntity(commentVo.getCreatedBy(), null, 1);
    }

    public void updateComment(CommentVo commentVo) {
        this.commentType = String.valueOf(commentVo.getCommentType());
        this.content = commentVo.getContent();
        this.setBaseEntity(null, commentVo.getModifiedBy(), 1);
    }

    public void deleteComment(CommentVo commentVo) {
        this.setBaseEntity(null, commentVo.getModifiedBy(), 0);
    }

}
