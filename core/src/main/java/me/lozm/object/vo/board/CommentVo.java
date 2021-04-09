package me.lozm.object.vo.board;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import me.lozm.object.code.CommentType;
import me.lozm.object.dto.board.CommentDeleteDto;
import me.lozm.object.dto.board.CommentPostDto;
import me.lozm.object.dto.board.CommentPutDto;
import me.lozm.object.vo.BaseVo;

@Getter @SuperBuilder
public class CommentVo extends BaseVo {

    private Long id;
    private CommentType commentType;
    private String content;
    private Long boardId;


    public static CommentVo of(CommentPostDto.Request reqDto) {
        return CommentVo.builder()
                .boardId(reqDto.getBoardId())
                .commentType(reqDto.getCommentType())
                .content(reqDto.getContent())
                .createdBy(reqDto.getCreatedBy())
                .build();
    }

    public static CommentVo of(CommentPutDto.Request reqDto) {
        return CommentVo.builder()
                .id(reqDto.getId())
                .commentType(reqDto.getCommentType())
                .content(reqDto.getContent())
                .modifiedBy(reqDto.getModifiedBy())
                .build();
    }

    public static CommentVo of(Long modifiedBy, CommentDeleteDto targetDto) {
        return CommentVo.builder()
                .id(targetDto.getId())
                .modifiedBy(modifiedBy)
                .build();
    }

}
