package me.lozm.domain.board.dto;

import lombok.*;
import me.lozm.global.common.BaseUserDto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CommentDeleteDto {
    @NotNull
    private Long id;

    @Getter @Setter
    public static class Request extends BaseUserDto {
        private List<CommentDeleteDto> list = new ArrayList<>();
    }

}
