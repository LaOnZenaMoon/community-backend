package me.lozm.domain.board.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.converter.CommentTypeConverter;
import me.lozm.global.common.BaseEntity;

import javax.persistence.*;

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

    @Column(name = "COMMENT_TYPE")
    @Convert(converter = CommentTypeConverter.class)
    private CommentType commentType;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

}
