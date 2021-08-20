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
        private UseYn useYn;
        private LocalDateTime createdDateTime;
        private Long createdUserId;
        private String createdUserIdentifier;
        private LocalDateTime modifiedDateTime;
        private Long modifiedUserId;
        private String modifiedUserIdentifier;

        public static BoardListInfo from(BoardVo.BoardList boardInfo) {
            return BoardListInfo.builder()
                    .id(boardInfo.getBoardId())
                    .boardType(boardInfo.getBoardType())
                    .contentType(boardInfo.getBoardContentType())
                    .title(boardInfo.getBoardTitle())
                    .content(boardInfo.getBoardContent())
                    .useYn(boardInfo.getBoardUse())
                    .createdDateTime(boardInfo.getBoardCreatedDateTime())
                    .createdUserId(boardInfo.getCreatedUserId())
                    .createdUserIdentifier(boardInfo.getCreatedUserIdentifier())
                    .modifiedDateTime(boardInfo.getBoardModifiedDateTime())
                    .modifiedUserId(boardInfo.getModifiedUserId())
                    .modifiedUserIdentifier(boardInfo.getModifiedUserIdentifier())
                    .build();
        }
    }

    @Getter
    public static class BoardList {
        Page<BoardListInfo> boardList;

        public static BoardList createBoardList(Page<BoardVo.BoardList> boardList) {
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
        private UseYn useYn;
        private LocalDateTime createdDateTime;
        private Long createdUserId;
        private String createdUserIdentifier;
        private LocalDateTime modifiedDateTime;
        private Long modifiedUserId;
        private String modifiedUserIdentifier;

        public static BoardInfo from(Board board) {
            final User createdUser = board.getCreatedUser().getId().equals(UsersType.SYSTEM.getCode()) ? User.from(UsersType.SYSTEM) : board.getCreatedUser();

            User modifiedUser;
            if (board.getModifiedUser() == null) {
                modifiedUser = User.builder().build();
            } else {
                modifiedUser = board.getModifiedUser().getId().equals(UsersType.SYSTEM.getCode()) ? User.from(UsersType.SYSTEM) : board.getModifiedUser();
            }

            return BoardInfo.builder()
                    .id(board.getId())
                    .boardType(board.getBoardType())
                    .contentType(board.getContentType())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .useYn(board.getUse())
                    .createdDateTime(board.getCreatedDateTime())
                    .createdUserId(createdUser.getId())
                    .createdUserIdentifier(createdUser.getIdentifier())
                    .modifiedDateTime(board.getModifiedDateTime())
                    .modifiedUserId(modifiedUser.getId())
                    .modifiedUserIdentifier(modifiedUser.getIdentifier())
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
