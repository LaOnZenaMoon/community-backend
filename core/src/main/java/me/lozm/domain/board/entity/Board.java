package me.lozm.domain.board.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.domain.board.dto.BoardDto;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.UseYn;
import me.lozm.global.code.converter.BoardTypeConverter;
import me.lozm.global.code.converter.ContentTypeConverter;
import me.lozm.global.common.BaseEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;

@Table(schema = "LOZM", name = "BOARD")
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "BOARD_SEQ_GEN", sequenceName = "BOARD_SEQ", initialValue = 1, allocationSize = 1)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GEN")
    @Column(name = "BOARD_ID")
    private Long id;

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


    public Board add(BoardDto.AddRequest requestDto) {
        return Board.builder()
                .boardType(requestDto.getBoardType())
                .contentType(requestDto.getContentType())
                .viewCount(0L)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .createdBy(requestDto.getCreatedBy())
                .use(UseYn.USE)
                .build();
    }

    public void edit(BoardType boardType, ContentType contentType, String title, String content, Long modifiedBy, UseYn useYn) {
        this.boardType = ObjectUtils.isEmpty(boardType) ? this.boardType : boardType;
        this.contentType = ObjectUtils.isEmpty(contentType) ? this.contentType : contentType;
        this.title = StringUtils.isEmpty(title) ? this.title : title;
        this.content = StringUtils.isEmpty(content) ? this.content : content;
        setModifiedBy(ObjectUtils.isEmpty(modifiedBy) ? getModifiedBy() : modifiedBy);
        setUse(ObjectUtils.isEmpty(useYn) ? getUse() : useYn);
    }

    public void addViewCount() {
        this.viewCount += 1;
    }

}
