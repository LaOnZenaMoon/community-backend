package me.lozm.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.vo.BoardVo;
import me.lozm.domain.user.entity.User;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.UseYn;
import me.lozm.global.code.UsersType;
import me.lozm.global.object.dto.BaseUserDto;
import me.lozm.global.object.entity.HierarchicalEntity;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BoardDto {

    @Getter
    @Builder
    public static class BoardListInfo {
        private Long id;
        private BoardType boardType;
        private ContentType contentType;
        private String title;
        private String content;
        private UseYn use;
        private LocalDateTime createdDateTime;
        private Long createdUserId;
        private String createdUserIdentifier;

        public static BoardListInfo from(BoardVo.ListInfo boardInfo) {
            return BoardListInfo.builder()
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
    public static class BoardList {
        Page<BoardListInfo> boardList;

        public static BoardList createBoardList(Page<BoardVo.ListInfo> boardList) {
            BoardList list = new BoardList();
            list.boardList = boardList.map(BoardListInfo::from);
            return list;
        }
    }

    @Getter
    @Builder
    public static class BoardInfo {
        private Long id;
        private BoardType boardType;
        private ContentType contentType;
        private String title;
        private String content;
        private UseYn use;
        private LocalDateTime createdDateTime;
        private Long createdUserId;
        private String createdUserIdentifier;

        public static BoardInfo from(Board board) {
            final User createdUser = board.getCreatedUser().getId().equals(UsersType.API_SYSTEM.getCode()) ? User.from(UsersType.API_SYSTEM) : board.getCreatedUser();

            return BoardInfo.builder()
                    .id(board.getId())
                    .boardType(board.getBoardType())
                    .contentType(board.getContentType())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .use(board.getUse())
                    .createdDateTime(board.getCreatedDateTime())
                    .createdUserId(createdUser.getId())
                    .createdUserIdentifier(createdUser.getIdentifier())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class AddRequest extends BaseUserDto {
        @NotNull
        private BoardType boardType;

        @NotNull
        private ContentType contentType;

        @NotNull
        private String title;

        @NotNull
        private String content;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class AddReplyRequest extends AddRequest {
        @NotNull
        private Long commonParentId;

        @NotNull
        private Long parentId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class EditRequest extends BaseUserDto {
        @NotNull
        private Long id;

        private BoardType boardType;

        private ContentType contentType;

        private String title;

        private String content;

        private UseYn useYn;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class RemoveRequest extends BaseUserDto {
        @NotNull
        private Long id;
    }

}
