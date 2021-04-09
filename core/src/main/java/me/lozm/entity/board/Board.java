package me.lozm.entity.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.entity.BaseEntity;
import me.lozm.object.vo.board.BoardVo;

import javax.persistence.*;
import java.util.List;

@Table(schema = "LOZM", name = "BOARD")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "BOARD_SEQ_GEN", sequenceName = "BOARD_SEQ", initialValue = 1, allocationSize = 1)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GEN")
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(name = "BOARD_TYPE")
    private String boardType;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    @Column(name = "TITLE")
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments;


    public void insertBoard(BoardVo boardVo) {
        this.boardType = String.valueOf(boardVo.getBoardType());
        this.contentType = String.valueOf(boardVo.getContentType());
        this.title = boardVo.getTitle();
        this.content = boardVo.getContent();
        this.setBaseEntity(boardVo.getCreatedBy(), null, 1);
    }

    public void updateBoard(BoardVo boardVo) {
        this.boardType = String.valueOf(boardVo.getBoardType());
        this.contentType = String.valueOf(boardVo.getContentType());
        this.title = boardVo.getTitle();
        this.content = boardVo.getContent();
        this.setBaseEntity(null, boardVo.getModifiedBy(), 1);
    }

    public void deleteBoard(BoardVo boardVo) {
        this.setBaseEntity(null, boardVo.getModifiedBy(), 0);
    }

}
