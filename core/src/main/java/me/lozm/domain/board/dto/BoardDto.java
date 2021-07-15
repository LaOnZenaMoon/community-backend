package me.lozm.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.vo.BoardVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.UseYn;
import me.lozm.global.common.BaseUserDto;
import me.lozm.global.common.HierarchicalEntity;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BoardDto {

    @Getter
    @Builder
    public static class ResponseListInfo {
        private Long id;
        private BoardType boardType;
        private ContentType contentType;
        private String title;
        private String content;
        private UseYn use;
        private LocalDateTime createdDateTime;
        private Long createdUserId;
        private String createdUserIdentifier;

        public static ResponseListInfo from(BoardVo.ListInfo boardInfo) {
            return ResponseListInfo.builder()
                    .id(boardInfo.getBoardId())
                    .boardType(boardInfo.getBoardType())
                    .contentType(boardInfo.getBoardContentType())
                    .title(boardInfo.getBoardTitle())
                    .content(boardInfo.getBoardContent())
                    .use(boardInfo.getBoardUse())
                    .createdDateTime(boardInfo.getBoardCreatedDateTime())
                    .createdUserId(boardInfo.getUserId())
                    .createdUserIdentifier(boardInfo.getUserIdentifier())
                    .build();
        }
    }

    @Getter
    public static class ResponseList {
        Page<ResponseListInfo> boardList;

        public static ResponseList createBoardList(Page<BoardVo.ListInfo> boardList) {
            ResponseList list = new ResponseList();
            list.boardList = boardList.map(ResponseListInfo::from);
            return list;
        }
    }

    @Getter
    @Builder
    public static class ResponseOne {
        private Long id;
        private BoardType boardType;
        private ContentType contentType;
        private String title;
        private String content;
        private UseYn use;

        public static ResponseOne from(Board board) {
            return ResponseOne.builder()
                    .id(board.getId())
                    .boardType(board.getBoardType())
                    .contentType(board.getContentType())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .use(board.getUse())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddRequest extends BaseUserDto {
        @NotNull
        private BoardType boardType;

        @NotNull
        private ContentType contentType;

        @NotNull
        private String title;

        @NotNull
        private String content;

        public static Board createEntity(AddRequest requestDto) {
            return Board.builder()
                    .hierarchicalBoard(HierarchicalEntity.createEntity())
                    .boardType(requestDto.getBoardType())
                    .contentType(requestDto.getContentType())
                    .viewCount(0L)
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .createdBy(requestDto.getCreatedBy())
                    .createdDateTime(LocalDateTime.now())
                    .use(UseYn.USE)
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddReplyRequest extends AddRequest {
        @NotNull
        private Long commonParentId;

        @NotNull
        private Long parentId;

        public static Board createEntity(AddReplyRequest requestDto) {
            return Board.builder()
                    .hierarchicalBoard(HierarchicalEntity.createEntity(requestDto.getCommonParentId(), requestDto.getParentId()))
                    .boardType(requestDto.getBoardType())
                    .contentType(requestDto.getContentType())
                    .viewCount(0L)
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .createdBy(requestDto.getCreatedBy())
                    .createdDateTime(LocalDateTime.now())
                    .use(UseYn.USE)
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditRequest extends BaseUserDto {
        @NotNull
        private Long id;

        private BoardType boardType;

        private ContentType contentType;

        private String title;

        private String content;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveRequest extends BaseUserDto {
        @NotNull
        private Long id;
    }

}
