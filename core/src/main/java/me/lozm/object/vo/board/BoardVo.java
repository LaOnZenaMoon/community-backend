package me.lozm.object.vo.board;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import me.lozm.object.code.BoardType;
import me.lozm.object.code.ContentType;
import me.lozm.object.dto.board.BoardDeleteDto;
import me.lozm.object.dto.board.BoardPostDto;
import me.lozm.object.dto.board.BoardPutDto;
import me.lozm.object.vo.BaseVo;

@Getter @SuperBuilder
public class BoardVo extends BaseVo {

    private Long id;
    private BoardType boardType;
    private ContentType contentType;
    private String title;
    private String content;


    public static BoardVo of(BoardPostDto.Request reqDto) {
        return BoardVo.builder()
                .boardType(reqDto.getBoardType())
                .contentType(reqDto.getContentType())
                .title(reqDto.getTitle())
                .content(reqDto.getContent())
                .createdBy(reqDto.getCreatedBy())
                .build();
    }

    public static BoardVo of(BoardPutDto.Request reqDto) {
        return BoardVo.builder()
                .id(reqDto.getId())
                .boardType(reqDto.getBoardType())
                .contentType(reqDto.getContentType())
                .title(reqDto.getTitle())
                .content(reqDto.getContent())
                .modifiedBy(reqDto.getModifiedBy())
                .build();
    }

    public static BoardVo of(Long modifiedBy, BoardDeleteDto targetDto) {
        return BoardVo.builder()
                .id(targetDto.getId())
                .modifiedBy(modifiedBy)
                .build();
    }

}
