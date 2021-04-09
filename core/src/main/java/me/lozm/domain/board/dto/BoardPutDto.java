package me.lozm.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.common.BaseUserDto;

import javax.validation.constraints.NotNull;

public class BoardPutDto {

    @Getter @Builder
    public static class Request extends BaseUserDto {
        @NotNull
        private Long id;

        @NotNull
        private BoardType boardType;

        @NotNull
        private ContentType contentType;

        private String title;

        private String content;
    }

}
