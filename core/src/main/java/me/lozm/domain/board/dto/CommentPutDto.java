package me.lozm.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import me.lozm.global.code.CommentType;
import me.lozm.global.common.BaseUserDto;

import javax.validation.constraints.NotNull;

public class CommentPutDto {

    @Getter @Builder
    public static class Request extends BaseUserDto {
        @NotNull
        private Long id;

        private CommentType commentType;

        private String content;
    }

}
