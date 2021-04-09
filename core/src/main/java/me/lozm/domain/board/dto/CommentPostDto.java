package me.lozm.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import me.lozm.global.code.CommentType;
import me.lozm.global.common.BaseUserDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CommentPostDto {

    @Getter @Builder
    public static class Request extends BaseUserDto {
        @NotNull
        private Long boardId;

        @NotNull
        private CommentType commentType;

        @NotEmpty
        private String content;
    }

}
