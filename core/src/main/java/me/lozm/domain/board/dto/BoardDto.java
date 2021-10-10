package me.lozm.domain.board.dto;

import io.swagger.annotations.ApiModelProperty;
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

import static me.lozm.global.swagger.BoardCode.*;
import static me.lozm.global.swagger.CommonCode.*;

public class BoardDto {

    @Getter
    @Builder
    public static class BoardListInfo {
        @ApiModelProperty(value = BOARD_ID_DESCRIPTION)
        private Long id;

        @ApiModelProperty(value = BOARD_TYPE_DESCRIPTION)
        private BoardType boardType;

        @ApiModelProperty(value = CONTENT_TYPE_DESCRIPTION)
        private ContentType contentType;

        @ApiModelProperty(value = TITLE_DESCRIPTION)
        private String title;

        @ApiModelProperty(value = CONTENT_DESCRIPTION)
        private String content;

        @ApiModelProperty(value = USE_YN_DESCRIPTION)
        private UseYn useYn;

        @ApiModelProperty(value = CREATED_DATETIME_DESCRIPTION)
        private LocalDateTime createdDateTime;

        @ApiModelProperty(value = CREATED_BY_DESCRIPTION)
        private Long createdUserId;

        @ApiModelProperty(value = CREATED_USER_LOGIN_ID_DESCRIPTION)
        private String createdUserIdentifier;

        @ApiModelProperty(value = MODIFIED_DATETIME_DESCRIPTION)
        private LocalDateTime modifiedDateTime;

        @ApiModelProperty(value = MODIFIED_BY_DESCRIPTION)
        private Long modifiedUserId;

        @ApiModelProperty(value = MODIFIED_USER_LOGIN_ID_DESCRIPTION)
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
        @ApiModelProperty(value = BOARD_ID_DESCRIPTION)
        private Long id;

        @ApiModelProperty(value = BOARD_TYPE_DESCRIPTION)
        private BoardType boardType;

        @ApiModelProperty(value = CONTENT_TYPE_DESCRIPTION)
        private ContentType contentType;

        @ApiModelProperty(value = TITLE_DESCRIPTION)
        private String title;

        @ApiModelProperty(value = CONTENT_DESCRIPTION)
        private String content;

        @ApiModelProperty(value = USE_YN_DESCRIPTION)
        private UseYn useYn;

        @ApiModelProperty(value = CREATED_DATETIME_DESCRIPTION)
        private LocalDateTime createdDateTime;

        @ApiModelProperty(value = CREATED_BY_DESCRIPTION)
        private Long createdUserId;

        @ApiModelProperty(value = CREATED_USER_LOGIN_ID_DESCRIPTION)
        private String createdUserIdentifier;

        @ApiModelProperty(value = MODIFIED_DATETIME_DESCRIPTION)
        private LocalDateTime modifiedDateTime;

        @ApiModelProperty(value = MODIFIED_BY_DESCRIPTION)
        private Long modifiedUserId;

        @ApiModelProperty(value = MODIFIED_USER_LOGIN_ID_DESCRIPTION)
        private String modifiedUserIdentifier;

        public static BoardInfo from(Board board) {
            User createdUser;
            if (board.getCreatedUser() == null) {
                createdUser = User.builder().build();
            } else if (board.getCreatedUser().getId().equals(UsersType.SYSTEM.getCode())) {
                createdUser = User.from(UsersType.SYSTEM);
            } else {
                createdUser = board.getCreatedUser();
            }

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
        @ApiModelProperty(value = BOARD_TYPE_DESCRIPTION, example = BOARD_TYPE_EXAMPLE)
        private BoardType boardType;

        @NotNull
        @ApiModelProperty(value = CONTENT_TYPE_DESCRIPTION, example = CONTENT_TYPE_EXAMPLE)
        private ContentType contentType;

        @NotNull
        @ApiModelProperty(value = TITLE_DESCRIPTION, example = TITLE_EXAMPLE)
        private String title;

        @NotNull
        @ApiModelProperty(value = CONTENT_DESCRIPTION, example = CONTENT_EXAMPLE)
        private String content;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class AddReplyRequest extends AddRequest {
        @NotNull
        @ApiModelProperty(value = BOARD_ID_DESCRIPTION)
        private Long commonParentId;

        @NotNull
        @ApiModelProperty(value = BOARD_ID_DESCRIPTION)
        private Long parentId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class EditRequest extends BaseUserDto {
        @NotNull
        @ApiModelProperty(value = BOARD_ID_DESCRIPTION)
        private Long id;

        @ApiModelProperty(value = BOARD_TYPE_DESCRIPTION, example = BOARD_TYPE_EXAMPLE)
        private BoardType boardType;

        @ApiModelProperty(value = CONTENT_TYPE_DESCRIPTION, example = CONTENT_TYPE_EXAMPLE)
        private ContentType contentType;

        @ApiModelProperty(value = TITLE_DESCRIPTION, example = TITLE_EXAMPLE)
        private String title;

        @ApiModelProperty(value = CONTENT_DESCRIPTION, example = CONTENT_EXAMPLE)
        private String content;

        @ApiModelProperty(value = USE_YN_DESCRIPTION, example = USE_YN_EXAMPLE)
        private UseYn useYn;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class RemoveRequest extends BaseUserDto {
        @NotNull
        @ApiModelProperty(value = BOARD_ID_DESCRIPTION)
        private Long id;
    }

}
