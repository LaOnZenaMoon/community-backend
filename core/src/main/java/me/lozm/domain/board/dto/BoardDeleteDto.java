package me.lozm.domain.board.dto;

import lombok.*;
import me.lozm.global.common.BaseUserDto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class BoardDeleteDto {

    @NotNull
    private Long id;


    @Getter @Setter
    public static class Request extends BaseUserDto {
        private List<BoardDeleteDto> list = new ArrayList<>();
    }

}
